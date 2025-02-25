import org.example.Assignment.FilterInvoice;
import org.example.Assignment.Invoice;
import org.example.Assignment.QueryInvoicesDAO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilterInvoiceTest {


        @Test
        void testFilterInvoice() {
            // test using the real database
            FilterInvoice filter = new FilterInvoice();
            List<Invoice> lowValueInvoices = filter.lowValueInvoices();

            // validation
            assertNotNull(lowValueInvoices);

            // Verify all returned invoices are low value
            for (Invoice invoice : lowValueInvoices) {
                assertTrue(invoice.getValue() < 100);
            }
        }

        @Test
        void filterInvoiceStubbedTest() {
            // Create mock for the DAO
            QueryInvoicesDAO mockDAO = mock(QueryInvoicesDAO.class);

            // Create test invoices
            Invoice lowInvoice1 = new Invoice("Customer1", 50);
            Invoice highInvoice1 = new Invoice("Customer2", 150);
            Invoice lowInvoice2 = new Invoice("Customer3", 75);
            Invoice highInvoice2 = new Invoice("Customer4", 200);

            List<Invoice> testInvoices = Arrays.asList(
                    lowInvoice1, highInvoice1, lowInvoice2, highInvoice2
            );

            // Stub the mock DAO
            when(mockDAO.all()).thenReturn(testInvoices);

            // Create the filter with the mock DAO
            FilterInvoice filter = new FilterInvoice(mockDAO);

            // Execute the method under test
            List<Invoice> result = filter.lowValueInvoices();

            // Verify results
            assertEquals(2, result.size());

            // Test that only invoices with value < 100 are included
            assertTrue(result.contains(lowInvoice1));
            assertTrue(result.contains(lowInvoice2));
            assertFalse(result.contains(highInvoice1));
            assertFalse(result.contains(highInvoice2));

            // Alternative verification using properties
            for (Invoice invoice : result) {
                assertTrue(invoice.getValue() < 100);
                assertTrue(invoice.getCustomer().equals("Customer1") ||
                        invoice.getCustomer().equals("Customer3"));
            }


        }
    }

