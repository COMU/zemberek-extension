package lo.tr.tools.spellchecker;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.*;
import com.sun.star.lib.uno.helper.ComponentBase;
import com.sun.star.linguistic2.*;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import lo.tr.tools.OneInstanceFactory;
import lo.tr.tools.XSpellAlternatives_impl;
import zemberek.core.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TurkishSpellChecker extends ComponentBase implements
        XSpellChecker,
        XLinguServiceEventBroadcaster,
        XInitialization,
        XServiceDisplayName,
        XServiceInfo {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static String serviceName = TurkishSpellChecker.class.getName();
    private static Locale turkishLocale = new Locale("tr", "TR", "");
    private static Locale defaultLocale = new Locale();

    private static String[] supportedServiceNames = {
            "com.sun.star.linguistic2.SpellChecker",
            "lo.tr.tools.spellchecker.TurkishSpellChecker"
    };

    private static ZemberekSpellCheck spellChecker =
            ZemberekSpellCheck.getInstance();

    PropChgHelper_Spell propertyChangeHelper;
    ArrayList<?> eventListeners;
    private boolean disposing;

    public TurkishSpellChecker() {
        // names of relevant properties to be used
        String[] aProps = new String[]{
                "IsIgnoreControlCharacters",
                "IsUseDictionaryList",
                "IsSpellUpperCase",
                "IsSpellWithDigits",
                "IsSpellCapitalization"};

        propertyChangeHelper = new PropChgHelper_Spell(this, aProps);
        eventListeners = new ArrayList<>();
        disposing = false;
    }

    private boolean isEqual(Locale l1, Locale l2) {
        return l1.Language.equals(l2.Language) &&
                l1.Country.equals(l2.Country) &&
                l1.Variant.equals(l2.Variant);
    }

    private boolean getValueToUse(
            String property,
            boolean defaultValue,
            PropertyValue[] properties) {
        boolean result = defaultValue;

        try {
            // use temporary value if supplied
            for (PropertyValue p : properties) {
                if (property.equals(p.Name)) {
                    if (AnyConverter.isBoolean(p.Value)) {
                        return AnyConverter.toBoolean(p.Value);
                    }
                }
            }

            // otherwise use value from property set (if available)
            XPropertySet xPropSet = propertyChangeHelper.GetPropSet();
            if (xPropSet != null)   // should always be the case
            {
                Object aObj = xPropSet.getPropertyValue(property);
                if (AnyConverter.isBoolean(aObj))
                    result = AnyConverter.toBoolean(aObj);
            }
        } catch (Exception e) {
            Log.warn("Exception occurred " + e.getMessage());
        }

        return result;
    }

    private boolean isUpper(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isUpperCase(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // __________ interface methods __________


    //XSupportedLocales

    private boolean hasDigits(String word) {
        for (int i = 0; i < word.length(); ++i) {
            if (Character.isDigit(word.charAt(i)))
                return true;
        }
        return false;
    }

    private short getSpellFailure(String word, Locale locale) {

        if (isEqual(locale, turkishLocale) && !spellChecker.isCorrect(word)) {
            return SpellFailure.SPELLING_ERROR;
        }
        return -1;
    }

    //XSpellChecker

    private XSpellAlternatives getProposals(
            String word,
            Locale locale) {

        String[] proposals = EMPTY_STRING_ARRAY;

        if (isEqual(locale, turkishLocale)) {
            List<String> proposalList = spellChecker.getSuggestions(word);
            if (proposalList.size() > 0) {
                proposals = proposalList.toArray(new String[0]);
            }
        }

        // always return a result if word is incorrect,
        // proposals may be empty though.
        return new XSpellAlternatives_impl(
                word,
                locale,
                SpellFailure.SPELLING_ERROR,
                proposals);

    }

    public Locale[] getLocales() {
        return new Locale[]{turkishLocale};
    }

    //XLinguServiceEventBroadcaster

    public boolean hasLocale(Locale locale) {
        return isEqual(locale, turkishLocale);
    }

    public boolean isValid(
            String word,
            Locale locale,
            PropertyValue[] properties) {

        if (isEqual(locale, defaultLocale) || word.length() == 0)
            return true;

        // linguistic is currently not allowed to throw exceptions
        // thus we return null which means 'word cannot be spelled'
        if (!hasLocale(locale))
            return true;

        // get values of relevant properties that may be used.
        //! The values for 'IsIgnoreControlCharacters' and 'IsUseDictionaryList'
        //! are handled by the dispatcher! Thus there is no need to access
        //! them here.
        boolean bIsSpellWithDigits = getValueToUse("IsSpellWithDigits", true, properties);
        boolean bIsSpellUpperCase = getValueToUse("IsSpellUpperCase", false, properties);
        boolean bIsSpellCapitalization = getValueToUse("IsSpellCapitalization", true, properties);

        short nFailure = getSpellFailure(word, locale);
        if (nFailure != -1) {
            // postprocess result for errors that should be ignored
            if ((!bIsSpellUpperCase && isUpper(word))
                    || (!bIsSpellWithDigits && hasDigits(word))
                    || (!bIsSpellCapitalization && nFailure == SpellFailure.CAPTION_ERROR)
            ) {
                nFailure = -1;
            }
        }

        return nFailure == -1;
    }

    // XServiceDisplayName

    public XSpellAlternatives spell(
            String word,
            Locale locale,
            PropertyValue[] properties)
            throws IllegalArgumentException {

        if (isEqual(locale, defaultLocale) || word.length() == 0)
            return null;

        // linguistic is currently not allowed to throw exceptions
        // thus we return null which means 'word cannot be spelled'
        if (!hasLocale(locale))
            return null;

        XSpellAlternatives xRes = null;
        if (!isValid(word, locale, properties)) {
            xRes = getProposals(word, locale);
        }
        return xRes;
    }


    // XInitialization

    public boolean addLinguServiceEventListener(XLinguServiceEventListener xLstnr) {
        if (!disposing && xLstnr != null) {
            return propertyChangeHelper.addLinguServiceEventListener(xLstnr);
        }
        return false;
    }

    // XServiceInfo

    public boolean removeLinguServiceEventListener(XLinguServiceEventListener xLstnr) {

        if (!disposing && xLstnr != null) {
            return propertyChangeHelper.removeLinguServiceEventListener(xLstnr);
        }
        return false;
    }

    public String getServiceDisplayName(Locale aLocale) {
        return "TurkishSpellChecker";
    }

    public void initialize(Object[] aArguments) {
        if (2 == aArguments.length) {
            XPropertySet xPropSet = UnoRuntime.queryInterface(XPropertySet.class, aArguments[0]);
            // start listening to property changes
            propertyChangeHelper.AddAsListenerTo(xPropSet);
        }
    }

    public boolean supportsService(String serviceName) {
        return Arrays.asList(supportedServiceNames).contains(serviceName);
    }

    public String getImplementationName() {
        return serviceName;
    }

    public String[] getSupportedServiceNames() {
        return supportedServiceNames;
    }

    /**
     * Returns a factory for creating the service.
     * This method is called by the <code>JavaLoader</code>
     * <p>
     *
     * @param aImplName        the name of the implementation for which a service is desired
     * @param xMultiFactoryTmp the service manager to be used if needed
     * @param xRegKey          the registryKey
     * @return returns a <code>XSingleServiceFactory</code> for creating the component
     * @see com.sun.star.comp.loader.JavaLoader
     */
    public static XSingleServiceFactory __getServiceFactory(String aImplName,
                                                            XMultiServiceFactory xMultiFactoryTmp,
                                                            com.sun.star.registry.XRegistryKey xRegKey) {
        XSingleServiceFactory xSingleServiceFactory = null;
        if (aImplName.equals(serviceName)) {
            xSingleServiceFactory = new OneInstanceFactory(
                    TurkishSpellChecker.class,
                    serviceName,
                    supportedServiceNames,
                    xMultiFactoryTmp);
        }
        return xSingleServiceFactory;
    }

    /**
     * Writes the service information into the given registry key.
     * This method is called by the <code>JavaLoader</code>
     * <p>
     *
     * @param xRegKey the registryKey
     * @return returns true if the operation succeeded
     * @see com.sun.star.comp.loader.JavaLoader
     */
    public static boolean __writeRegistryServiceInfo(
            com.sun.star.registry.XRegistryKey xRegKey) {
        boolean bResult = true;
        String[] aServices = supportedServiceNames;
        int i, nLength = aServices.length;
        for (i = 0; i < nLength; ++i) {
            bResult = bResult && com.sun.star.comp.loader.FactoryHelper.writeRegistryServiceInfo(
                    serviceName, aServices[i], xRegKey);
        }
        return bResult;
    }
}
