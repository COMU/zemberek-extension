package lo.tr.tools;

import com.sun.star.beans.PropertyChangeEvent;
import com.sun.star.beans.XPropertyChangeListener;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.EventObject;
import com.sun.star.linguistic2.LinguServiceEvent;
import com.sun.star.linguistic2.XLinguServiceEventBroadcaster;
import com.sun.star.linguistic2.XLinguServiceEventListener;
import com.sun.star.uno.XInterface;
import java.util.ArrayList;

public class PropChgHelper implements
    XPropertyChangeListener,
    XLinguServiceEventBroadcaster {

  private final XInterface xEvtSource;
  private final String[] propertyNames;
  private final ArrayList<XLinguServiceEventListener> aLngSvcEvtListeners;
  private XPropertySet xPropSet;

  public PropChgHelper(
      XInterface xEvtSource,
      String[] aPropNames) {
    this.xEvtSource = xEvtSource;
    this.propertyNames = aPropNames;
    xPropSet = null;
    aLngSvcEvtListeners = new ArrayList<>();
  }

  public XInterface getEvtSource() {
    return xEvtSource;
  }

  public XPropertySet getPropSet() {
    return xPropSet;
  }


  public void launchEvent(LinguServiceEvent aEvt) {

    for (XLinguServiceEventListener xLstnr : aLngSvcEvtListeners) {
      if (xLstnr != null) {
        xLstnr.processLinguServiceEvent(aEvt);
      }
    }
  }

  public void addAsListenerTo(XPropertySet xPropertySet) {

    // do not listen any longer to the old property set (if any)
    removeAsListener();

    // set new property set to be used and register as listener to it
    xPropSet = xPropertySet;
    if (xPropSet != null) {
      for (String propName : propertyNames) {
        if (propName.length() != 0) {
          try {
            xPropSet.addPropertyChangeListener(propName, this);
          } catch (Exception e) {
            System.out.println("Exception in PropertyChgHelper.addAsListenerTo " + e.getMessage());
            // do nothing
          }
        }
      }
    }
  }

  private void removeAsListener() {
    if (xPropSet != null) {
      for (String propertyName : propertyNames) {
        if (propertyName.length() != 0) {
          try {
            xPropSet.removePropertyChangeListener(propertyName, this);
          } catch (Exception e) {
            System.out.println("Exception in PropertyChgHelper.removeAsListener " + e.getMessage());
            // do nothing
          }
        }
      }
      xPropSet = null;
    }
  }

  // __________ interface methods __________

  // XEventListener

  public void disposing(EventObject aSource)
      throws com.sun.star.uno.RuntimeException {
    if (aSource.Source == xPropSet) {
      removeAsListener();
    }
  }

  // XPropertyChangeListener

  public void propertyChange(PropertyChangeEvent aEvt)
      throws com.sun.star.uno.RuntimeException {
    // will be overridden in derived classes
  }

  // XLinguServiceEventBroadcaster
  public boolean addLinguServiceEventListener(
      XLinguServiceEventListener xListener)
      throws com.sun.star.uno.RuntimeException {
    return xListener != null && aLngSvcEvtListeners.add(xListener);
  }

  public boolean removeLinguServiceEventListener(
      XLinguServiceEventListener xListener)
      throws com.sun.star.uno.RuntimeException {

    if (xListener != null) {
      int nIdx = aLngSvcEvtListeners.indexOf(xListener);
      if (nIdx != -1) {
        aLngSvcEvtListeners.remove(nIdx);
        return true;
      }
    }
    return false;
  }
}
