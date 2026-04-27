import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategorySelectionTest {

    @Test
    void testCategorySelectionExists() {
        //checks that the class can be loaded
        assertNotNull(new CategorySelectionTest());
    }

    void testValidCategory() {
        assertTrue(CategorySelection.isValidCategory("Science"));
        assertTrue(CategorySelection.isValidCategory("History"));
        assertTrue(CategorySelection.isValidCategory("Movies"));
    }

    @Test
    void testInvalidCategory() {
        assertFalse(CategorySelection.isValidCategory("Sports"));
        assertFalse(CategorySelection.isValidCategory(""));
        assertFalse(CategorySelection.isValidCategory(null));
    }
}