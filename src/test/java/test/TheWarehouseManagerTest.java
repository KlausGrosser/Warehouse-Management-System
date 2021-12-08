package test;

import data.Employee;
import data.Item;
import data.Warehouse;
import org.junit.jupiter.api.*;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TheWarehouseManagerTest {

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    @BeforeAll
    public static void  setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void checkOrderPrint() {
        Employee employee = new Employee();
        employee.order("Brand new keyboard",4);
        assertEquals("\nOrdered 4 Brand new keyboards", outContent.toString());
    }

    @Test
    public void err() {
        System.err.print("hello again");
        assertEquals("hello again", errContent.toString());
    }

    @Test
    void checkWarehouseDetails(){
        Warehouse one = new Warehouse(1);
        Warehouse two = new Warehouse(2);
        one.addItem(new Item("brand new", "keyboard", 0, new Date()));
        assertEquals(1, one.occupancy());
        assertEquals(0, two.occupancy());
        assertEquals("[Brand new keyboard]", one.getStock().toString());
    }


}