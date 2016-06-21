package de.haw.informatik.devsupport.wp1516.audime.time;

/**
 * Created by Tim on 22.11.2015.
 */
public interface ITimeService {
    /**
     *
     * @return die aktuelle Zeit die mit dem Sender-Device synchronisiert wurde. Beim Sender-Device ist es die aktuelle Systemzeit
     */
    long getTime();

    /**
     * St��t die Synchronisierung der Zeit zu dem mitgegebenen roundTrip an.
     * Es werden alle roundTrips zu gleichen Teilen in die Synchronisation aufgenommen.
     * Au�nahmen sind dabei Ausrei�er, also alle roundTrips die einen Offset produzieren, der um mehr als 10 ms vom aktuellen Offset abweicht.
     * Ausrei�er werden erst nach dem 10. roundTrip ausgewertet. Davor werden alle roundTrips aufgenommen
     *
     * @param roundTrip - Objekt in dem die Zeit des Sendens der Anfrage und der "Serverzeit" des Sender-Device gekapselt wird.
     */
    void syncTime(TimeSyncRoundTrip roundTrip);

    /**
     * Setzt alle Werte der Zeitsynchronisation zur�ck
     */
    void reset();

    /**
     * Getter die nur f�r das Debugging ben�tigt werden
     * @return
     */
    long getOffset();

    /**
     * Getter die nur f�r das Debugging ben�tigt werden
     * @return
     */
    double getDeviation();

    /**
     * Setzt den Offset der Uhr
     * @param offset
     */
    void setOffset(long offset);
}
