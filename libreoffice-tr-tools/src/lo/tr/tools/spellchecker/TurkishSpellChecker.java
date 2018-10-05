package lo.tr.tools.spellchecker;

/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*- */
/*************************************************************************
 *
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *
 *  Copyright 2000, 2010 Oracle and/or its affiliates.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of Sun Microsystems, Inc. nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 *  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 *  USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *************************************************************************/

// uno

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

import java.util.ArrayList;
import java.util.List;

public class TurkishSpellChecker extends ComponentBase implements
        XSpellChecker,
        XLinguServiceEventBroadcaster,
        XInitialization,
        XServiceDisplayName,
        XServiceInfo {

    public static String _aSvcImplName = TurkishSpellChecker.class.getName();
    static Locale turkishLocale = new Locale("tr", "TR", "");
    static Locale defaultLocale = new Locale();

    //TODO: change this with Zemberek later.
    private static DummyTurkishLinguist dummyTurkishLinguist = new DummyTurkishLinguist();

    PropChgHelper_Spell aPropChgHelper;
    ArrayList<?> aEvtListeners;
    boolean bDisposing;

    public TurkishSpellChecker() {
        // names of relevant properties to be used
        String[] aProps = new String[]
                {
                        "IsIgnoreControlCharacters",
                        "IsUseDictionaryList",
                        "IsSpellUpperCase",
                        "IsSpellWithDigits",
                        "IsSpellCapitalization"
                };
        aPropChgHelper = new PropChgHelper_Spell(this, aProps);
        aEvtListeners = new ArrayList<Object>();
        bDisposing = false;
    }

    public static String[] getSupportedServiceNames_Static() {
        String[] aResult = {
                "com.sun.star.linguistic2.SpellChecker",
                "lo.tr.tools.spellchecker.TurkishSpellChecker",
        };
        return aResult;
    }

    private boolean IsEqual(Locale aLoc1, Locale aLoc2) {
        return aLoc1.Language.equals(aLoc2.Language) &&
                aLoc1.Country.equals(aLoc2.Country) &&
                aLoc1.Variant.equals(aLoc2.Variant);
    }

    private boolean GetValueToUse(
            String aPropName,
            boolean bDefaultVal,
            PropertyValue[] aProps) {
        boolean bRes = bDefaultVal;

        try {
            // use temporary value if supplied
            for (int i = 0; i < aProps.length; ++i) {
                if (aPropName.equals(aProps[i].Name)) {
                    Object aObj = aProps[i].Value;
                    if (AnyConverter.isBoolean(aObj)) {
                        bRes = AnyConverter.toBoolean(aObj);
                        return bRes;
                    }
                }
            }

            // otherwise use value from property set (if available)
            XPropertySet xPropSet = aPropChgHelper.GetPropSet();
            if (xPropSet != null)   // should always be the case
            {
                Object aObj = xPropSet.getPropertyValue(aPropName);
                if (AnyConverter.isBoolean(aObj))
                    bRes = AnyConverter.toBoolean(aObj);
            }
        } catch (Exception e) {
            bRes = bDefaultVal;
        }

        return bRes;
    }

    private boolean IsUpper(String aWord, Locale aLocale) {
        java.util.Locale aLang = new java.util.Locale(
                aLocale.Language, aLocale.Country, aLocale.Variant);
        return aWord.equals(aWord.toUpperCase(aLang));
    }

    // __________ interface methods __________


    //XSupportedLocales

    private boolean HasDigits(String aWord) {
        int nLen = aWord.length();
        for (int i = 0; i < nLen; ++i) {
            if (Character.isDigit(aWord.charAt(i)))
                return true;
        }
        return false;
    }

    private short GetSpellFailure(
            String aWord,
            Locale aLocale,
            PropertyValue[] aProperties) {
        short nRes = -1;
        if (IsEqual(aLocale, turkishLocale)) {
            if (!dummyTurkishLinguist.isCorrect(aWord)) {
                nRes = SpellFailure.SPELLING_ERROR;
            }
        }
        return nRes;
    }


    //XSpellChecker

    private XSpellAlternatives GetProposals(
            String aWord,
            Locale aLocale,
            PropertyValue[] aProperties) {

        short nType = SpellFailure.SPELLING_ERROR;
        String[] aProposals = null;

        if (IsEqual(aLocale, turkishLocale)) {
            List<String> proposalList = dummyTurkishLinguist.getSuggestions(aWord);
            aProposals = proposalList.toArray(new String[0]);
            if (aProposals.length == 0) {
                aProposals = new String[1];
            }
        }

        // always return a result if word is incorrect,
        // proposals may be empty though.
        return new XSpellAlternatives_impl(aWord, aLocale,
                nType, aProposals);

    }

    public Locale[] getLocales()
            throws com.sun.star.uno.RuntimeException {
        return new Locale[]{turkishLocale};
    }


    //XLinguServiceEventBroadcaster

    public boolean hasLocale(Locale aLocale)
            throws com.sun.star.uno.RuntimeException {
        boolean bRes = false;
        if (IsEqual(aLocale, turkishLocale)) bRes = true;
        return bRes;
    }

    public boolean isValid(
            String aWord, Locale aLocale,
            PropertyValue[] aProperties)
            throws com.sun.star.uno.RuntimeException,
            IllegalArgumentException {
        if (IsEqual(aLocale, defaultLocale) || aWord.length() == 0)
            return true;

        // linguistic is currently not allowed to throw exceptions
        // thus we return null which means 'word cannot be spelled'
        if (!hasLocale(aLocale))
            return true;

        // get values of relevant properties that may be used.
        //! The values for 'IsIgnoreControlCharacters' and 'IsUseDictionaryList'
        //! are handled by the dispatcher! Thus there is no need to access
        //! them here.
        boolean bIsSpellWithDigits = GetValueToUse("IsSpellWithDigits", false, aProperties);
        boolean bIsSpellUpperCase = GetValueToUse("IsSpellUpperCase", false, aProperties);
        boolean bIsSpellCapitalization = GetValueToUse("IsSpellCapitalization", true, aProperties);

        short nFailure = GetSpellFailure(aWord, aLocale, aProperties);
        if (nFailure != -1) {
            // postprocess result for errors that should be ignored
            if ((!bIsSpellUpperCase && IsUpper(aWord, aLocale))
                    || (!bIsSpellWithDigits && HasDigits(aWord))
                    || (!bIsSpellCapitalization
                    && nFailure == SpellFailure.CAPTION_ERROR)
            )
                nFailure = -1;
        }

        return nFailure == -1;
    }


    // XServiceDisplayName

    public XSpellAlternatives spell(
            String aWord, Locale aLocale,
            PropertyValue[] aProperties)
            throws com.sun.star.uno.RuntimeException,
            IllegalArgumentException {
        if (IsEqual(aLocale, defaultLocale) || aWord.length() == 0)
            return null;

        // linguistic is currently not allowed to throw exceptions
        // thus we return null fwhich means 'word cannot be spelled'
        if (!hasLocale(aLocale))
            return null;

        XSpellAlternatives xRes = null;
        if (!isValid(aWord, aLocale, aProperties)) {
            xRes = GetProposals(aWord, aLocale, aProperties);
        }
        return xRes;
    }


    // XInitialization

    public boolean addLinguServiceEventListener(
            XLinguServiceEventListener xLstnr)
            throws com.sun.star.uno.RuntimeException {
        boolean bRes = false;
        if (!bDisposing && xLstnr != null)
            bRes = aPropChgHelper.addLinguServiceEventListener(xLstnr);
        return bRes;
    }


    // XServiceInfo

    public boolean removeLinguServiceEventListener(
            XLinguServiceEventListener xLstnr)
            throws com.sun.star.uno.RuntimeException {
        boolean bRes = false;
        if (!bDisposing && xLstnr != null)
            bRes = aPropChgHelper.removeLinguServiceEventListener(xLstnr);
        return bRes;
    }

    public String getServiceDisplayName(Locale aLocale)
            throws com.sun.star.uno.RuntimeException {
        return "TurkishSpellChecker";
    }

    public void initialize(Object[] aArguments)
            throws com.sun.star.uno.Exception,
            com.sun.star.uno.RuntimeException {
        int nLen = aArguments.length;
        if (2 == nLen) {
            XPropertySet xPropSet = UnoRuntime.queryInterface(
                    XPropertySet.class, aArguments[0]);
            // start listening to property changes
            aPropChgHelper.AddAsListenerTo(xPropSet);
        }
    }

    // __________ static things __________

    public boolean supportsService(String aServiceName)
            throws com.sun.star.uno.RuntimeException {
        String[] aServices = getSupportedServiceNames_Static();
        int i, nLength = aServices.length;
        boolean bResult = false;

        for (i = 0; !bResult && i < nLength; ++i)
            bResult = aServiceName.equals(aServices[i]);

        return bResult;
    }

    public String getImplementationName()
            throws com.sun.star.uno.RuntimeException {
        return _aSvcImplName;
    }

    public String[] getSupportedServiceNames()
            throws com.sun.star.uno.RuntimeException {
        return getSupportedServiceNames_Static();
    }

    private static XMultiServiceFactory xMultiFactory;

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
        xMultiFactory = xMultiFactoryTmp;
        XSingleServiceFactory xSingleServiceFactory = null;
        if (aImplName.equals(_aSvcImplName)) {
            xSingleServiceFactory = new OneInstanceFactory(
                    TurkishSpellChecker.class,
                    _aSvcImplName,
                    getSupportedServiceNames_Static(),
                    xMultiFactory);
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
        String[] aServices = getSupportedServiceNames_Static();
        int i, nLength = aServices.length;
        for (i = 0; i < nLength; ++i) {
            bResult = bResult && com.sun.star.comp.loader.FactoryHelper.writeRegistryServiceInfo(
                    _aSvcImplName, aServices[i], xRegKey);
        }
        return bResult;
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */