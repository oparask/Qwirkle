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
        //grid.add(45, 45, bluecross);
        assertThrows(QwirkleException.class, () -> grid.add(45, 45, bluecross));
    }

    @Test
    void add_a_tile_outside_virtual_grid() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var tile = new Tile(BLUE, SQUARE);
        //grid.add(0, 30, tile);
        assertThrows(QwirkleException.class, () -> grid.add(0, 30, tile));
        //grid.add(90, 30, tile);
        assertThrows(QwirkleException.class, () -> grid.add(90, 30, tile));
        //grid.add(30, 0, tile);
        assertThrows(QwirkleException.class, () -> grid.add(30, 0, tile));
        //grid.add(30, 90, tile);
        assertThrows(QwirkleException.class, () -> grid.add(30, 90, tile));
    }

    @Test
    void add_a_tile_to_a_position_already_taken() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile bluecross = new Tile(BLUE, STAR);
        Tile bluesquare = new Tile(BLUE, SQUARE);
        grid.add(45, 46, bluecross);
        //grid.add(45, 46, bluesquare);
        assertThrows(QwirkleException.class, () -> grid.add(45, 46, bluesquare));
    }

    @Test
    void add_a_tile_not_connected() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile bluecross = new Tile(BLUE, CROSS);
        //grid.add(3, 3, bluecross);
        assertThrows(QwirkleException.class, () -> grid.add(3, 3, bluecross));
    }

    @Test
    void add_a_tile_notSameColorOrShape() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile redstar = new Tile(RED, STAR);
        //grid.add(45, 44, redstar);
        assertThrows(QwirkleException.class, () -> grid.add(45, 44, redstar));
    }

    @Test
    void add_a_tile_notSameColorOrShape_Horizontal() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44, new Tile(RED, ROUND));
        grid.add(45, 43, new Tile(RED, DIAMOND));
        grid.add(45, 42, new Tile(RED, PLUS));
        grid.add(45, 46, new Tile(RED, SQUARE));
        //grid.add(45, 41, new Tile(YELLOW, STAR));
        assertThrows(QwirkleException.class, () -> grid.add(45, 41, new Tile(YELLOW, STAR)));
    }

    @Test
    void add_a_tile_notSameColorOrShape_Vertical() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44, new Tile(RED, ROUND));
        grid.add(45, 43, new Tile(RED, DIAMOND));
        grid.add(45, 42, new Tile(RED, PLUS));
        grid.add(45, 46, new Tile(RED, SQUARE));
        //grid.add(45, 41, new Tile(YELLOW, STAR));
        assertThrows(QwirkleException.class, () -> grid.add(45, 41, new Tile(YELLOW, STAR)));
    }

    @Test
    void add_a_tile_full_line_vertical() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(44, 45, new Tile(RED, ROUND));
        grid.add(43, 45, new Tile(RED, DIAMOND));
        grid.add(42, 45, new Tile(RED, PLUS));
        grid.add(41, 45, new Tile(RED, SQUARE));
        grid.add(46, 45, new Tile(RED, STAR));
        //grid.add(47, 45, new Tile(RED, STAR));
        assertThrows(QwirkleException.class, () -> grid.add(47, 45, new Tile(RED, STAR)));
    }

    @Test
    void add_a_tile_full_line_horizontal() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44, new Tile(RED, ROUND));
        grid.add(45, 43, new Tile(RED, DIAMOND));
        grid.add(45, 42, new Tile(RED, PLUS));
        grid.add(45, 46, new Tile(RED, SQUARE));
        grid.add(45, 47, new Tile(RED, STAR));
        //grid.add(45, 48, new Tile(RED, STAR));
        assertThrows(QwirkleException.class, () -> grid.add(45, 48, new Tile(RED, STAR)));
    }


    //second add method

    //Checks that the grid is not empty because if it is, the firstAdd method must be used;
    @Test
    void add_tiles_grid_empty() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        //grid.add(33, 33, RIGHT, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(33, 33, RIGHT, t1, t2));
    }

    //Checks that the position of the first and last tile is valid:
    //all "row" and "col" must be in the grid, i.e. 91*91;
    @Test
    void add_tiles_outside_virtual_grid() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        //grid.add(0, 30, RIGHT, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(0, 30, RIGHT, t1, t2));
        //grid.add(4, 89, RIGHT, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(4, 89, RIGHT, t1, t2));
        //grid.add(91, 30, LEFT, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(91, 30, LEFT, t1, t2));
        //grid.add(4, 0, LEFT, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(4, 0, LEFT, t1, t2));
        //grid.add(0, 1, UP, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(0, 1, UP, t1, t2));
        //grid.add(1, 1, UP, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(1, 1, UP, t1, t2));
        //grid.add(90, 1, DOWN, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(90, 1, DOWN, t1, t2));
        //grid.add(89, 1, DOWN, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(89, 1, DOWN, t1, t2));

    }

    //Checks that at this position of the grid does not already exist a tile;
    @Test
    void add_tiles_to_a_position_already_taken() {
        grid.firstAdd(RIGHT, new Tile(BLUE, CROSS));
        Tile bluecross = new Tile(BLUE, STAR);
        Tile bluesquare = new Tile(BLUE, SQUARE);
        grid.add(45, 46,DOWN,bluesquare, bluecross);
        assertThrows(QwirkleException.class, () ->  grid.add(45, 46,DOWN,bluesquare, bluesquare));
    }

    //Checks that the tiles you want to add respect the rules of the Qwrikle game:
    //Checks that the tiles to add are adjacent to an existing tile on the grid;
    @Test
    void add_several_tiles_not_connected() {
        grid.firstAdd(RIGHT, new Tile(BLUE, DIAMOND));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        assertThrows(QwirkleException.class, () -> grid.add(44, 43, DOWN, t1, t2, t3));
        assertThrows(QwirkleException.class, () -> grid.add(44, 43, UP, t1, t2, t3));
        assertThrows(QwirkleException.class, () -> grid.add(44, 43, LEFT, t1, t2, t3));
        assertThrows(QwirkleException.class, () -> grid.add(44, 30, RIGHT, t1, t2, t3));
    }

    //Checks that the horizontal and vertical tile line is composed of a maximum of 6 tiles;
    //attention on a pas encore tester pour UP ans DOWN horizontalement
    @Test
    void add_tiles_tests_UP() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        var t4 = new Tile(RED, STAR);
        var t5 = new Tile(RED, SQUARE);
        var t6 = new Tile(RED, CROSS);
        //tiles not connected
        //grid.add(43, 45,UP, t1, t2, t3, t4, t5, t6);
        assertThrows(QwirkleException.class, () -> grid.add(43, 45,UP, t1, t2, t3, t4, t5, t6));
        //vertical line already complete
        //grid.add(44, 45,UP, t1, t2, t3, t4, t5, t6);
        assertThrows(QwirkleException.class, () -> grid.add(44, 45,UP, t1, t2, t3, t4, t5, t6));
        //not same color or shape
        var t7 = new Tile(BLUE, CROSS);
        //grid.add(44, 45,UP, t1, t2, t3, t4, t7);
        assertThrows(QwirkleException.class, () -> grid.add(44, 45,UP, t1, t2, t3, t4, t7));
        //same tile
        //grid.add(44, 45,UP, t1, t2, t3, t4, t6);
        assertThrows(QwirkleException.class, () -> grid.add(44, 45,UP, t1, t2, t3, t4, t6));
        //normal case
        grid.add(44, 45,UP, t1, t2, t3, t4, t5);
        //tile at this position
        //grid.add(44, 45,UP, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(44, 45,UP, t1, t2));

    }
    @Test
    void add_tiles_tests_DOWN() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        var t4 = new Tile(RED, STAR);
        var t5 = new Tile(RED, SQUARE);
        var t6 = new Tile(RED, CROSS);
        //tiles not connected
        //grid.add(47, 45,DOWN, t1, t2, t3, t4, t5, t6);
        assertThrows(QwirkleException.class, () -> grid.add(47, 45, DOWN, t1, t2, t3, t4, t5, t6));
        //vertical line already complete
        //grid.add(46, 45,DOWN, t1, t2, t3, t4, t5, t6);
        assertThrows(QwirkleException.class, () -> grid.add(46, 45, DOWN, t1, t2, t3, t4, t5, t6));
        //not same color or shape
        var t7 = new Tile(BLUE, CROSS);
        //grid.add(46, 45,DOWN, t1, t2, t3, t4, t7);
        assertThrows(QwirkleException.class, () -> grid.add(46, 45, DOWN, t1, t2, t3, t4, t7));
        //same tile
        //grid.add(46, 45,DOWN, t1, t2, t3, t4, t6);
        assertThrows(QwirkleException.class, () -> grid.add(46, 45, DOWN, t1, t2, t3, t4, t6));
        //normal case
        grid.add(46, 45, DOWN, t1, t2, t3, t4, t5);
        //tile at this position
        //grid.add(46, 45,DOWN, t1, t2);
        assertThrows(QwirkleException.class, () -> grid.add(46, 45,DOWN, t1, t2));
    }
    //Checks that the horizontal and vertical tiles share the same characteristic, color or sahpe.
    @Test
    void add_tiles_notSameColorOrShape() {
        grid.firstAdd(RIGHT, new Tile(BLUE, DIAMOND));
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(BLUE, PLUS);
        //grid.add(45, 44, UP, t1, t2, t3);
        assertThrows(QwirkleException.class, () -> grid.add(45, 44, UP, t1, t2, t3));
    }

    @Test
    void add_tiles_notSameColorOrShape_Horizontal() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44, new Tile(RED, ROUND));
        grid.add(45, 43, new Tile(RED, DIAMOND));
        grid.add(45, 42, new Tile(RED, PLUS));
        grid.add(45, 46, new Tile(RED, SQUARE));
        //grid.add(45, 41, new Tile(YELLOW, STAR));
        assertThrows(QwirkleException.class, () -> grid.add(45, 41, new Tile(YELLOW, STAR)));
    }

    @Test
    void add_tiles_notSameColorOrShape_Vertical() {
        grid.firstAdd(RIGHT, new Tile(RED, CROSS));
        grid.add(45, 44, new Tile(RED, ROUND));
        grid.add(45, 43, new Tile(RED, DIAMOND));
        grid.add(45, 42, new Tile(RED, PLUS));
        grid.add(45, 46, new Tile(RED, SQUARE));
        //grid.add(45, 41, new Tile(YELLOW, STAR));
        assertThrows(QwirkleException.class, () -> grid.add(45, 41, new Tile(YELLOW, STAR)));
    }

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
        rules_sonia_a();
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
        rules_sonia_a();
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(PURPLE, SQUARE);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 45, RIGHT, t1, t2, t3);
        });
    }

    @Test
    void rules_Elvire_c() {
        rules_cédric_b();
        var t1 = new Tile(BLUE, ROUND);
        grid.add(45, 46, t1);
        assertEquals(t1, grid.get(45, 46));
    }

    @Test
    void rules_Elvire_c_adapted_to_fail() {
        rules_cédric_b();
        var t2 = new Tile(BLUE, ROUND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(45, 45, t2);
        });
    }

    @Test
    void rules_Vincent_d() {
        rules_Elvire_c();
        var t1 = new Tile(GREEN, PLUS);
        var t2 = new Tile(GREEN, DIAMOND);
        grid.add(43, 44, DOWN, t1, t2);
        assertEquals(t1, grid.get(43, 44));
        assertEquals(t2, grid.get(44, 44));
    }

    @Test
    void rules_Vincent_d_adapted_to_fail() {
        rules_Elvire_c();
        var t3 = new Tile(GREEN, CROSS);
        var t4 = new Tile(GREEN, DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            grid.add(43, 44, DOWN, t3, t4);
        });
    }

    //third add method
    @Test
    void tileAtPosition() {
        grid.firstAdd(RIGHT, new Tile(YELLOW, SQUARE));
        var t1 = new TileAtPosition(45, 44, new Tile(RED, SQUARE));
        var t2 = new TileAtPosition(45, 46, new Tile(BLUE, SQUARE));
        grid.add(t1, t2);
    }

    @Test
    void tileAtPosition_different_sameTile() {
        grid.firstAdd(RIGHT, new Tile(YELLOW, SQUARE));
        var t1 = new TileAtPosition(45, 44, new Tile(YELLOW, SQUARE));
        var t2 = new TileAtPosition(45, 46, new Tile(BLUE, SQUARE));
        assertThrows(QwirkleException.class, () -> {
            grid.add(t1, t2);
        });
    }

    @Test
    void tileAtPosition_different_notSameLine() {
        grid.firstAdd(RIGHT, new Tile(RED, SQUARE), new Tile(RED, DIAMOND));
        var t1 = new TileAtPosition(45, 44, new Tile(RED, CROSS));
        var t2 = new TileAtPosition(46, 46, new Tile(RED, ROUND));
        assertThrows(QwirkleException.class, () -> {
            grid.add(t1, t2);
        });
    }
    @Test
    void tileAtPosition_different_notConnected() {
        grid.firstAdd(RIGHT, new Tile(RED, SQUARE), new Tile(RED, DIAMOND));
        var t1 = new TileAtPosition(45, 44, new Tile(RED, CROSS));
        var t2 = new TileAtPosition(47, 44, new Tile(RED, ROUND));
        assertThrows(QwirkleException.class, () -> {
            grid.add(t1, t2);
        });
    }
    @Test
    void tileAtPosition_different_Connected() {
        grid.firstAdd(RIGHT, new Tile(RED, SQUARE), new Tile(RED, DIAMOND));
        var t1 = new TileAtPosition(45, 44, new Tile(RED, CROSS));
        var t2 = new TileAtPosition(45, 43, new Tile(RED, ROUND));
        grid.add(t1, t2);
    }

    //Score tests
    @Test
    void test_score_FirstAdd() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        var t4 = new Tile(RED, STAR);
        var t5 = new Tile(RED, SQUARE);
        var t6 = new Tile(RED, CROSS);
        assertEquals(12,  grid.firstAdd( DOWN, t1, t2, t3, t4, t5, t6));
    }
    @Test
    void score_sonia_A() {
        var t1 = new Tile(RED, ROUND);
        var t2 = new Tile(RED, DIAMOND);
        var t3 = new Tile(RED, PLUS);
        assertEquals(3, grid.firstAdd(UP, t1, t2, t3));
    }

    @Test
    void score_Cédric_B() {
        score_sonia_A();
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        var t3 = new Tile(PURPLE, SQUARE);
        assertEquals(7,  grid.add(46, 45, RIGHT, t1, t2, t3));
        var t4 = new TileAtPosition(46, 45, t1);
        var t5 = new TileAtPosition(46, 46, t2);
        var t6 = new TileAtPosition(46, 47, t3);
        //assertEquals(7, grid.add(t4, t5, t6));
    }

    @Test
    void score_Elvire_C() {
        score_Cédric_B();
        var t1 = new Tile(BLUE, ROUND);
        assertEquals(4,  grid.add(45, 46, t1));
        var t2 = new TileAtPosition(45, 46, t1);
        //assertEquals(4,  grid.add(t2));
    }

    @Test
    void score_Vincent_D() {
        score_Elvire_C();
        var t1 = new Tile(GREEN, PLUS);
        var t2 = new Tile(GREEN, DIAMOND);
        assertEquals(6, grid.add(43, 44, DOWN, t1, t2));
        var t3 = new TileAtPosition(43, 44, t1);
        var t4 = new TileAtPosition(44, 44, t2);
        //assertEquals(6, grid.add(t3, t4));
    }

    @Test
    void score_sonia_E() {
        score_Vincent_D();
        var t1 = new TileAtPosition(42, 44,new Tile(GREEN, STAR));
        var t2 = new TileAtPosition(45, 44, new Tile(GREEN, ROUND));
        assertEquals(7, grid.add(t2, t1));
    }
    @Test
    void score_Cédric_F() {
        score_sonia_E();
        var t1 = new Tile(RED, SQUARE);
        var t2 = new Tile(ORANGE, SQUARE);
        assertEquals(6,  grid.add(47, 48, UP, t1, t2));
        var t3 = new TileAtPosition(47, 48, t1);
        var t4 = new TileAtPosition(46, 48, t2);
        //assertEquals(6, grid.add(t3, t4));
    }
    @Test
    void score_Elvire_G() {
        score_Cédric_F();
        var t1 = new Tile(YELLOW, STAR);
        var t2 = new Tile(ORANGE, STAR);
        assertEquals(3,  grid.add(42, 43, LEFT, t1, t2));
        var t3 = new TileAtPosition(42, 43, t1);
        var t4 = new TileAtPosition(42, 42, t2);
        //assertEquals(3, grid.add(t3, t4));
    }

    @Test
    void score_Vincent_H() {
        score_Elvire_G();
        var t1 = new Tile(ORANGE, CROSS);
        var t2 = new Tile(ORANGE, DIAMOND);
        assertEquals(3,  grid.add(43, 42, DOWN, t1, t2));
        var t3 = new TileAtPosition(43, 42, t1);
        var t4 = new TileAtPosition(44, 42, t2);
        //assertEquals(3, grid.add(t3, t4));
    }

    @Test
    void score_Sonia_I() {
        score_Vincent_H();
        var t1 = new Tile(YELLOW, DIAMOND);
        var t2 = new Tile(YELLOW, ROUND);
        assertEquals(10,  grid.add(44, 43, DOWN, t1, t2));
        var t3 = new TileAtPosition(44, 43, t1);
        var t4 = new TileAtPosition(45, 43, t2);
        //assertEquals(10, grid.add(t3, t4));
    }
    @Test
    void score_Cédric_J() {
        score_Sonia_I();
        var t1 = new Tile(RED, STAR);
        assertEquals(9,  grid.add(42, 45, t1));
        var t3 = new TileAtPosition(42, 45, t1);
        //assertEquals(9, grid.add(t3));
    }

    @Test
    void score_Elvire_K() {
        score_Cédric_J();
        var t1 = new Tile(ORANGE, CROSS);
        var t2 = new Tile(RED, CROSS);
        var t3 = new Tile(BLUE, CROSS);
        assertEquals(18,  grid.add(47, 44, RIGHT,t1, t2, t3));
        var t4 = new TileAtPosition(47, 44, t1);
        var t5 = new TileAtPosition(47, 45, t2);
        var t6 = new TileAtPosition(47, 46, t3);
        //assertEquals(18, grid.add(t4, t5, t6));
    }
    @Test
    void score_Vincent_L() {
        score_Elvire_K();
        var t1 = new Tile(YELLOW, SQUARE);
        var t2 = new Tile(BLUE, SQUARE);
        assertEquals(9,  grid.add(46, 49, DOWN,t1, t2));
        var t3 = new TileAtPosition(46, 49, t1);
        var t4 = new TileAtPosition(47, 49, t2);
        //assertEquals(9, grid.add(t3, t4));
    }
}