package org.example.Assignment;

import java.util.List;
import static java.util.stream.Collectors.toList;

public class FilterInvoice {
    private QueryInvoicesDAO dao;

    // Original constructor - kept for backward compatibility
    public FilterInvoice() {
        Database db = new Database();
        this.dao = new QueryInvoicesDAO(db);
    }

    // New constructor that accepts Database - provides partial DI
    public FilterInvoice(Database db) {
        this.dao = new QueryInvoicesDAO(db);
    }

    // New constructor that accepts DAO directly - provides complete DI
    public FilterInvoice(QueryInvoicesDAO dao) {
        this.dao = dao;
    }

    public List<Invoice> lowValueInvoices() {
        List<Invoice> all = dao.all();
        return all.stream()
                .filter(invoice -> invoice.getValue() < 100)
                .collect(toList());
    }
}