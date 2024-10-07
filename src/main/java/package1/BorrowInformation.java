package package1;

import javax.swing.text.Document;
import java.util.Date;

public class BorrowInformation {
    private int borrowId;
    private Document document;
    private Date borrowDate;
    private Date dueDate;
    private boolean isReturned;
    private double finesDue;

    public BorrowInformation(int _borrowId, Document _document, Date _borrowDate, Date _dueDate, boolean _isReturned) {
        this.borrowId = _borrowId;
        this.document = _document;
        this.borrowDate = _borrowDate;
        this.dueDate = _dueDate;
        this.isReturned = _isReturned;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int _borrowId) {
        this.borrowId = _borrowId;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document _document) {
        this.document = _document;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date _borrowDate) {
        this.borrowDate = _borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date _dueDate) {
        this.dueDate = _dueDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean _isReturned) {
        this.isReturned = _isReturned;
    }

    public double getFinesDue() {
        return finesDue;
    }

    public void setFinesDue(double _finesDue) {
        this.finesDue = _finesDue;
    }

    public void display() {
    }
}
