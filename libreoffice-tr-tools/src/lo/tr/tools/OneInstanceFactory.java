package lo.tr.tools;

import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XInitialization;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleServiceFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import java.lang.reflect.Constructor;

//
// purpose of this class is to provide a service factory that instantiates
// the services only once (as long as this factory itself exists)
// and returns only reference to that instance.
//
@SuppressWarnings("unchecked")
public class OneInstanceFactory implements
    XSingleServiceFactory,
    XServiceInfo {

  private Class aMyClass;
  private String serviceImplementationName;
  private String[] supportedServiceNames;
  private XInterface xInstantiatedService;
  private XMultiServiceFactory xMultiFactory;

  public OneInstanceFactory(
      Class aMyClass,
      String serviceImplementationName,
      String[] supportedServiceNames,
      XMultiServiceFactory xMultiFactory) {
    this.aMyClass = aMyClass;
    this.serviceImplementationName = serviceImplementationName;
    this.supportedServiceNames = supportedServiceNames;
    this.xMultiFactory = xMultiFactory;
    xInstantiatedService = null;
  }


  // XSingleServiceFactory interfaces.

  public Object createInstance()
      throws com.sun.star.uno.Exception,
      com.sun.star.uno.RuntimeException {
    if (xInstantiatedService == null) {
      //!! the here used services all have exact one constructor!!
      Constructor[] constructors = aMyClass.getConstructors();
      if (constructors == null) {
        System.out.println("Oops.. no constructor found?");
      } else {
        System.out.println("Count of constructors: " + constructors.length);
        try {
          xInstantiatedService =
              (XInterface) constructors[0].newInstance((java.lang.Object[]) null);
        } catch (java.lang.Exception e) {
          e.printStackTrace();
          throw new com.sun.star.uno.RuntimeException("Cannot load service.", e);
        }
      }

      //!! workaround for services not always being created
      //!! via 'createInstanceWithArguments'
      XInitialization xIni = UnoRuntime.queryInterface(
          XInitialization.class, createInstance());
      if (xIni != null) {
        Object[] aArguments = new Object[]{null, null};
        if (xMultiFactory != null) {
          XPropertySet xPropSet = UnoRuntime.queryInterface(
              XPropertySet.class, xMultiFactory.createInstance(
                  "com.sun.star.linguistic2.LinguProperties"));
          aArguments[0] = xPropSet;
        }
        xIni.initialize(aArguments);
      }
    }
    return xInstantiatedService;
  }

  public Object createInstanceWithArguments(Object[] aArguments)
      throws com.sun.star.uno.Exception,
      com.sun.star.uno.RuntimeException {
    if (xInstantiatedService == null) {
      XInitialization xIni = UnoRuntime.queryInterface(
          XInitialization.class, createInstance());
      if (xIni != null) {
        xIni.initialize(aArguments);
      }
    }
    return xInstantiatedService;
  }

  // XServiceInfo interfaces.

  public boolean supportsService(String aServiceName)
      throws com.sun.star.uno.RuntimeException {
    for (String name : supportedServiceNames) {
      if (aServiceName.equals(name)) {
        return true;
      }
    }
    return false;
  }

  public String getImplementationName()
      throws com.sun.star.uno.RuntimeException {
    return serviceImplementationName;
  }

  public String[] getSupportedServiceNames()
      throws com.sun.star.uno.RuntimeException {
    return supportedServiceNames;
  }
};