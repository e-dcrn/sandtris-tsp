package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.paint.Color;
import java.util.List;

public class GameTest {

    @Test
    void testAnimateParticles() {
        // À implémenter
    }

    @Test
    void testCheckCollision() {

        // quand ça touche le bas
        SandArea testsandarea = new SandArea(10, 10, 2);
        int newX = 2;
        int newY = 9;

        if (!testsandarea.checkCollision(newX, newY)) {
            fail("newY >= height - squareRatio");
        }

        // quand il y a un bloc déjà présent
        testsandarea.createBlock(4, 4, Color.RED);
        boolean result = testsandarea.checkCollision(4, 3);
        assertTrue(result, "Collision attendue avec un bloc déjà présent");

        // espace suffisant
        result = testsandarea.checkCollision(1, 1);
        assertFalse(result, "Pas de collision attendue : zone vide");
    }

    @Test
    void testfindCluster() {

        //tous les voisins sont de la meme couleurs
        SandArea area = new SandArea(10, 10, 1);
        Block a = new Block(1, 1, Color.RED);
        Block b = new Block(2, 1, Color.RED);
        Block c = new Block(3, 1, Color.RED);

        area.setBlock(a);
        area.setBlock(b);
        area.setBlock(c);

        List<List<Block>> clusters = area.findCluster();
        assertEquals(1, clusters.size(), "Il devrait y avoir un seul cluster");

        List<Block> cluster = clusters.get(0);
        assertEquals(3, cluster.size(), "Le cluster devrait contenir 3 blocs");
        assertTrue(cluster.contains(a));
        assertTrue(cluster.contains(b));
        assertTrue(cluster.contains(c));

        //aucun voisin de la meme couleur
        SandArea area2 = new SandArea(10, 10, 1);
        Block d = new Block(1, 1, Color.RED);
        Block e = new Block(2, 1, Color.BLUE);
        Block f = new Block(3, 1, Color.GREEN);

        area2.setBlock(d);
        area2.setBlock(e);
        area2.setBlock(f);

        List<List<Block>> clusters2 = area2.findCluster();
        assertEquals(3, clusters2.size(), "Chaque bloc devrait former son propre cluster");

        for (List<Block> cluster2 : clusters2) {
            assertEquals(1, cluster2.size(), "Chaque cluster doit contenir un seul bloc");
        }
    }

    @Test
    void testCreateParticle() {
        // À implémenter
    }

    @Test
    void testGetHeight() {
        // À implémenter
    }

    @Test
    void testGetParticles() {
        // À implémenter
    }

    @Test
    void testGetSquareSize() {
        // À implémenter
    }

    @Test
    void testGetWidth() {
        // À implémenter
    }
}
