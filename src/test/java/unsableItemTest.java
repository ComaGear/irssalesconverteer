import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.colbertlum.UnUsableItemMapper;
import com.colbertlum.entity.UnsableItem;

public class unsableItemTest {
    
    @Test
    public void loadUnsableItems(){
        UnUsableItemMapper unUsableItemMapper = new UnUsableItemMapper();
        List<UnsableItem> items = unUsableItemMapper.findItems("A16");
        assertTrue(items.size() > 0);
        assertTrue(items.get(0).getUnsableId() == "A16");
    }
}
