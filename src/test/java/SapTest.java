import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.Assignment.FilterInvoice;
import org.example.Assignment.Invoice;
import org.example.Assignment.SAP;
import org.example.Assignment.SAP_BasedInvoiceSender;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SapTest {

    @Test
    void testWhenLowInvoicesSent() {
        // Create mocks for dependencies
        FilterInvoice mockFilter = mock(FilterInvoice.class);
        SAP mockSAP = mock(SAP.class);

        // Create test data
        List<Invoice> lowValueInvoices = Arrays.asList(
                new Invoice("Customer1", 50),
                new Invoice("Customer2", 75)
        );

        // Stub the filter to return your test data
        when(mockFilter.lowValueInvoices()).thenReturn(lowValueInvoices);

        // Create the object under test with the mocks
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilter, mockSAP);

        // Call the method to test
        sender.sendLowValuedInvoices();

    }

    @Test
    void testWhenNoInvoices() {
        // Create mocks for dependencies
        FilterInvoice mockFilter = mock(FilterInvoice.class);
        SAP mockSAP = mock(SAP.class);

        // Stub the filter to return an empty list
        when(mockFilter.lowValueInvoices()).thenReturn(new ArrayList<>());

        // Create the object under test with the mocks
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilter, mockSAP);

        // Call the method to test
        sender.sendLowValuedInvoices();

    }
}