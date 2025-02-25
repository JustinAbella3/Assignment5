import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.Assignment.*;
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

    @Test
    void testThrowExceptionWhenBadInvoice() {
        // Create mocks for dependencies
        FilterInvoice mockFilter = mock(FilterInvoice.class);
        SAP mockSAP = mock(SAP.class);

        // Create test invoices
        Invoice goodInvoice = new Invoice("Customer1", 50);
        Invoice badInvoice = new Invoice("Customer2", 75);  // This one will trigger exception
        Invoice anotherGoodInvoice = new Invoice("Customer3", 60);

        List<Invoice> lowValueInvoices = Arrays.asList(
                goodInvoice, badInvoice, anotherGoodInvoice
        );

        // filter to return test invoices
        when(mockFilter.lowValueInvoices()).thenReturn(lowValueInvoices);

        // SAP to throw an exception for the specific bad invoice
        doThrow(new FailToSendSAPInvoiceException())
                .when(mockSAP).send(badInvoice);

        // Create the object under test
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilter, mockSAP);

        // Call the method and capture the failed invoices
        List<Invoice> failedInvoices = sender.sendLowValuedInvoices();

        // Verify the failed invoices list contains only the bad invoice
        assertEquals(1, failedInvoices.size());
        assertTrue(failedInvoices.contains(badInvoice));

        // Verify that SAP.send() was called for all invoices
        verify(mockSAP).send(goodInvoice);
        verify(mockSAP).send(badInvoice);
        verify(mockSAP).send(anotherGoodInvoice);
    }


}