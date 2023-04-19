package g60085.qwirkle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static g60085.qwirkle.model.Color.*;
import static g60085.qwirkle.model.Direction.*;
import static g60085.qwirkle.model.Shape.*;
import static org.junit.jupiter.api.Assertions.*;
//import static g60085.qwirkle.model.QwirkleTestUtils.*;

class GridTest {
    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid();
    }

    @Test
    @DisplayName("get outside the grid should return null, not throw")
    void can_get_tile_outside_virtual_grid() {
        var g = new Grid();
        assertDoesNotThrow(() -> grid.get(-250, 500));
        assertNull(grid.get(-250, 500));
    }

    @Test
    void firstAdd_one_tile() {
        var tile = new Tile(BLUE, CROSS);
        grid.firstAdd(RIGHT, tile);
        assertSame(grid.get(45, 45), tile);
    }

    @Test
    void firstAdd_multiple_tile() {
        var tile = new Tile(BLUE, CROSS);
        var tile2 = new Tile(BLUE, SQUARE);
        grid.firstAdd(RIGHT, tile, tile2);
        assertSame(grid.get(45, 45), tile);
        assertSame(grid.get(45, 46), tile2);
    }
    @Test
    void firstAdd_notEmpty() {
        var tile = new Tile(BLUE, CROSS);
        var tile2 = new Tile(BLUE, SQUARE);
        grid.firstAdd(RIGHT, tile);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(RIGHT, tile2));
    }
    @Test
    void firstAdd_invalidDirection() {
        var tile = new Tile(BLUE, CROSS);
        var tile2 = new Tile(BLUE, SQUARE);
        Direction d = null;
        assertThrows(QwirkleException.class, () -> grid.firstAdd(d, tile, tile2));
    }
    @Test
    void firstAdd_0_tile() {
        Tile[] tile = null;
        assertThrows(QwirkleException.class, () -> grid.firstAdd(RIGHT, tile));
    }

    @Test
    void firstAdd_tile_not_initialized() {
        Tile tile = null;
        assertThrows(QwirkleException.class, () -> grid.firstAdd(RIGHT, tile));
    }

    @Test
    void firstAdd_above_the_limit() {
        var tile = new Tile(BLUE, CROSS);
        var tile2 = new Tile(BLUE, SQUARE);
        var tile3 = new Tile(BLUE, DIAMOND);
        var tile4 = new Tile(BLUE, ROUND);
        var tile5 = new Tile(BLUE, PLUS);
        var tile6 = new Tile(BLUE, STAR);
        var tile7 = new Tile(RED, STAR);
        assertThrows(QwirkleException.class, () -> grid.firstAdd(RIGHT, tile, tile2, tile3, tile4, tile5, tile6, tile7));
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


    //les tests de la premiere methode add
    @Test
    void add_one_tile() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile bluesquare = new Tile(BLUE, SQUARE);
        grid.add(45, 46, bluesquare);
        assertEquals(bluesquare, grid.get(45, 46));
    }

    @Test
    void add_one_tile_grid_empty() {
        Tile bluecross = new Tile(BLUE, CROSS);
        assertThrows(QwirkleException.class, () -> grid.add(45, 45, bluecross));
    }

    @Test
    void add_tiles_outside_virtual_grid() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var tile = new Tile(BLUE, SQUARE);
        assertThrows(QwirkleException.class, () -> grid.add(-1, 30, tile));
        assertThrows(QwirkleException.class, () -> grid.add(91, 30, tile));
        assertThrows(QwirkleException.class, () -> grid.add(30, -1, tile));
        assertThrows(QwirkleException.class, () -> grid.add(30,91, tile));
    }
    @Test
    void add_a_tile_to_a_position_already_taken() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile bluecross = new Tile(BLUE, STAR);
        Tile bluesquare = new Tile(BLUE, SQUARE);
        grid.add(45, 46, bluecross);
        assertThrows(QwirkleException.class, () -> grid.add(45, 46, bluesquare));
    }
    @Test
    void add_a_tile_not_connected() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile bluecross = new Tile(BLUE, CROSS);
        assertThrows(QwirkleException.class, () -> grid.add(3, 3, bluecross));
    }

    @Test
    void add_a_tile_notSameColorOrShape() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile redstar = new Tile(RED, STAR);
        assertThrows(QwirkleException.class, () -> grid.add(45, 44, redstar));
    }
    @Test
    void add_a_tile_notSameColorOrShape_Horizontal() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44,new Tile(RED, ROUND) );
        grid.add(45, 43,new Tile(RED, DIAMOND) );
        grid.add(45, 42,new Tile(RED, PLUS) );
        grid.add(45, 46,new Tile(RED, SQUARE) );
        assertThrows(QwirkleException.class, () -> grid.add(45, 41, new Tile(YELLOW, STAR)));
    }
    @Test
    void add_a_tile_notSameColorOrShape_Vertical() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44,new Tile(RED, ROUND) );
        grid.add(45, 43,new Tile(RED, DIAMOND) );
        grid.add(45, 42,new Tile(RED, PLUS) );
        grid.add(45, 46,new Tile(RED, SQUARE) );
        assertThrows(QwirkleException.class, () -> grid.add(45, 41, new Tile(YELLOW, STAR)));
    }

    @Test
    void add_a_tile_full_line_vertical() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(44, 45,new Tile(RED, ROUND) );
        grid.add(43, 45,new Tile(RED, DIAMOND) );
        grid.add(42, 45,new Tile(RED, PLUS) );
        grid.add(41, 45,new Tile(RED, SQUARE) );
        grid.add(46, 45,new Tile(RED, STAR) );
        assertThrows(QwirkleException.class, () -> grid.add(47, 45, new Tile(RED, STAR)));
    }
    @Test
    void add_a_tile_full_line_horizontal() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44,new Tile(RED, ROUND) );
        grid.add(45, 43,new Tile(RED, DIAMOND) );
        grid.add(45, 42,new Tile(RED, PLUS) );
        grid.add(45, 46,new Tile(RED, SQUARE) );
        grid.add(45, 47,new Tile(RED, STAR) );
        assertThrows(QwirkleException.class, () -> grid.add(45, 48, new Tile(RED, STAR)));
    }

    //2eme methode add


    @Test
    void add_several_tiles() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        grid.add(3, 3, UP, t1, t2, t3);
        assertEquals(t1, grid.get(3, 3));
        assertEquals(t2, grid.get(2, 3));
        assertEquals(t3, grid.get(1, 3));
    }




    //add several tiles

    @Test
    void add_above_limit() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        var t4 = new Tile(RED, SQUARE);
        var t5 = new Tile(RED, STAR);
        var t6 = new Tile(RED, CROSS);
        var t7 = new Tile(RED, CROSS);
        grid.add(3, 3, UP, t1, t2, t3);
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
        assertThrows(QwirkleException.class, () -> {
            grid.firstAdd(UP, t1, t2, t3);
        });
        assertNull(grid.get(45, 45));
        assertNull(grid.get(44, 45));
        assertNull(grid.get(43, 45));
    }

    @Test
    void rules_cédric_b() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(PURPLE, SQUARE);
        grid.add(46, 45, RIGHT, t1, t2, t3);
        assertEquals(t1, grid.get(46, 45));
        assertEquals(t2, grid.get(46, 46));
        assertEquals(t3, grid.get(46, 47));
    }

    @Test
    void rules_cédric_b_adapted_to_fail() {
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(PURPLE, SQUARE);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 45, RIGHT, t1, t2, t3);
        });
        assertNull(grid.get(45, 45));
        assertNull(grid.get(45, 46));
        assertNull(grid.get(45, 47));
    }

    @Test
    void rules_Elvire_c() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t1);
        assertEquals(t1, grid.get(45, 46));
    }

    @Test
    void rules_Elvire_c_adapted_to_fail() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(RED, SQUARE);
        grid.add(40, 40, t1);
        var t2 = new Tile(BLUE, ROUND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(40, 40, t2);
        });
        assertEquals(t1, grid.get(40, 40));
    }

    @Test
    void rules_Vincent_d() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(GREEN, CROSS);
        var t2 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t1, t2);
        assertEquals(t1, grid.get(43, 44));
        assertEquals(t2, grid.get(44, 44));
    }

    @Test
    void rules_Vincent_d_adapted_to_fail() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(ORANGE, ROUND);
        var t2 = new Tile(ORANGE, SQUARE);
        grid.add(44, 44, DOWN, t1, t2);
        var t3 = new Tile(GREEN, CROSS);
        var t4 = new Tile(GREEN, DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(43, 44, DOWN, t3, t4);
        });
        assertEquals(t1, grid.get(44, 44));
        assertEquals(t2, grid.get(45, 44));
        assertEquals(t3, grid.get(43, 44));
    }

    //third add method
    @Test
    void tileAtPosition() {
        var t1 = new TileAtPosition(44, 44, new Tile(RED, SQUARE));
        var t2 = new TileAtPosition(43, 47, new Tile(BLUE, SQUARE));
        grid.add(t1, t2);
        assertEquals(t1.tile(), grid.get(44, 44));
        assertEquals(t2.tile(), grid.get(43, 47));
    }

    @Test
    void tileAtPosition_different_colorAndSpahe() {
        var t1 = new TileAtPosition(44, 44, new Tile(RED, DIAMOND));
        var t2 = new TileAtPosition(43, 47, new Tile(BLUE, SQUARE));
        var t3 = new TileAtPosition(43, 47, new Tile(BLUE, ROUND));
        assertThrows(QwirkleException.class, () -> {
            grid.add(t1, t2, t3);
        });
    }


    //les tests de la premiere methode add







}