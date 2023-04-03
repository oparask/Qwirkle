package g60085.qwirkle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static g60085.qwirkle.model.Color.*;
import static g60085.qwirkle.model.Direction.*;
import static g60085.qwirkle.model.Shape.*;
//import static g60085.qwirkle.model.QwirkleTestUtils.*;

class GridTest {
    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid();
    }

    @Test
    void firstAdd_one_tile() {
        var tile = new Tile(BLUE, CROSS);
        grid.firstAdd(RIGHT, tile);
        assertSame(grid.get(45, 45), tile);
    }

    @Test
    void firstAdd_cannot_be_called_twice() {
        Tile redcross = new Tile(RED, CROSS);
        Tile reddiamond = new Tile(RED, DIAMOND);
        grid.firstAdd(UP, redcross, reddiamond);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(DOWN, redcross, reddiamond));
    }

    //add
    @Test
    void firstAdd_must_be_called_first_simple() {
        Tile redcross = new Tile(RED, CROSS);
        assertThrows(QwirkleException.class, () -> grid.add(45, 45, redcross));
    }

       /* @Test
        @DisplayName("get outside the grid should return null, not throw")
        void can_get_tile_outside_virtual_grid() {
            var g = new Grid();
            assertDoesNotThrow(() -> grid.get(-250, 500));
            assertNull(grid.get(-250, 500));
        }*/
    @Test
    void add_one_tile() {
        Tile bluecross = new Tile(BLUE, CROSS);
        grid.add(3, 3, bluecross);
        assertEquals(bluecross, grid.get(3, 3));
    }
    @Test
    void add_a_tile_to_a_position_already_taken() {
        Tile bluecross = new Tile(BLUE, CROSS);
        Tile redsquare = new Tile(RED, SQUARE);
        grid.add(3, 3, bluecross);
        assertThrows(QwirkleException.class, () -> grid.add(3,3, redsquare));
    }

    //add several tiles
    @Test
    void add_several_tiles() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.add(3,3, UP, t1, t2, t3);
        assertEquals(t1, grid.get(3, 3));
        assertEquals(t2, grid.get(2, 3));
        assertEquals(t3, grid.get(1, 3));
    }
    @Test
    void add_7_tiles() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(RED, ROUND);
        var t6 = new Tile(RED, CROSS);
        var t7 = new Tile(RED, CROSS);
        grid.add(3,3, UP, t1, t2, t3);

    }


    //general cases
    @Test
    void rules_sonia_a() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.firstAdd(UP, t1, t2, t3);
        assertEquals(t1, grid.get(45, 45));
        assertEquals(t2, grid.get(44, 45));
        assertEquals(t3, grid.get(43, 45));
    }
    @Test
    void rules_sonia_a_adapted_to_fail() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, DIAMOND);
        assertThrows(QwirkleException.class, ()->{
            grid.firstAdd(UP, t1, t2, t3);
        });
        assertNull(grid.get(45,45));
        assertNull(grid.get(44,45));
        assertNull(grid.get(43,45));
    }

    @Test
    void rules_cédric_b() {
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(PURPLE, SQUARE);
        grid.add(46,45,RIGHT, t1, t2, t3);
        assertEquals(t1, grid.get(46, 45));
        assertEquals(t2, grid.get(46, 46));
        assertEquals(t3, grid.get(46, 47));
    }
    @Test
    void rules_cédric_b_adapted_to_fail() {
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(PURPLE, SQUARE);
        assertThrows(QwirkleException.class, ()->{
            grid.add(45,45,RIGHT, t1, t2, t3);
        });
        assertNull(grid.get(45, 45));
        assertNull(grid.get(45, 46));
        assertNull(grid.get(45, 47));
    }

    @Test
    void rules_Elvire_c() {
        var t1 = new Tile(BLUE, ROUND);
        grid.add(45,46, t1);
        assertEquals(t1, grid.get(45, 46));
    }
    @Test
    void rules_Elvire_c_adapted_to_fail() {
        var t1 = new Tile(RED, SQUARE);
        grid.add(40, 40, t1);
        var t2 = new Tile(BLUE, ROUND);
        assertThrows(QwirkleException.class, ()->{
            grid.add(40,40, t2);
        });
        assertEquals(t1, grid.get(40, 40));
    }
    @Test
    void rules_Vincent_d() {
        var t1 = new Tile(GREEN, CROSS);
        var t2 = new Tile(GREEN, DIAMOND);
        grid.add(43,44,DOWN, t1, t2);
        assertEquals(t1, grid.get(43, 44));
        assertEquals(t2, grid.get(44, 44));
    }
    @Test
    void rules_Vincent_d_adapted_to_fail() {
        var t1 = new Tile(ORANGE, ROUND);
        var t2 = new Tile(ORANGE, SQUARE);
        grid.add(44, 44, DOWN, t1, t2);
        var t3 = new Tile(GREEN, CROSS);
        var t4 = new Tile(GREEN, DIAMOND);
        assertThrows(QwirkleException.class, ()->{
            grid.add(43,44,DOWN, t3, t4);
        });
        assertEquals(t1, grid.get(44, 44));
        assertEquals(t2, grid.get(45, 44));
        assertEquals(t3, grid.get(43,44));
    }
}