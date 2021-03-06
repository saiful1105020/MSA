package org.uma.jmetalmsa.objective.impl;

import org.uma.jmetalmsa.score.impl.PercentageOfNonGapsScore;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetalmsa.solution.MSASolution;
import org.uma.jmetalmsa.util.distancematrix.impl.PAM250;

import mockit.Expectations;
import mockit.Mocked;

import static org.junit.Assert.*;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class PercentageOfNonGapsObjectiveTest {
  private static final double EPSILON = 0.00000000001;
  private PercentageOfNonGapsScore objective;

  @Mocked
  MSASolution solution;

  @Before
  public void startup() {
    objective = new PercentageOfNonGapsScore();
  }

  @Test
  public void shouldComputeReturnOneHundredIfTheSequenceHasNoGaps() {
    /* Assumed sequences:
          char[][]decodedSequence = new char[][]{
              {'B', 'C', 'D'},
              {'B', 'E', 'C'},
              {'L', 'L', 'L'},
              {'T', 'C', 'D'},
      };
     */
    new Expectations() {{
      solution.getNumberOfGaps();
      result = 0;
      times = 1;

      solution.getNumberOfVariables();
      result = 4;
      times = 1;

      solution.getAlignmentLength();
      result = 3;
      times = 1;
    }};

    assertEquals(100.0, objective.compute(solution, solution.decodeToMatrix()), EPSILON);
  }

  @Test
  public void shouldComputeReturnTheCorrectValueIfTheSequenceHasTwoGaps() {
    /* Assumed sequences:
          char[][]decodedSequence = new char[][]{
              {'B', 'C', 'D', '-'},
              {'B', '-', 'C', 'D'},
              {'L', 'L', 'L', 'G'},
              {'T', 'C', 'D', 'B'},
      };
     */
    new Expectations() {{
      solution.getNumberOfGaps();
      result = 2;
      times = 1;

      solution.getNumberOfVariables();
      result = 4;
      times = 1;

      solution.getAlignmentLength();
      result = 4;
      times = 1;
    }};

    assertEquals(100.0 * 14.0/(4*4), objective.compute(solution, solution.decodeToMatrix()), EPSILON);
  }

  @Test
  public void shouldComputeReturnTheCorrectValueIfTheSequenceHasManyGaps() {
    /* Assumed sequences:
        B----CD-
        B-CD----
        LL----LG
        --TC--DB
        -BC-B-HE
     */

    new Expectations() {{
      solution.getNumberOfGaps();
      result = 21;
      times = 1;

      solution.getNumberOfVariables();
      result = 5;
      times = 1;

      solution.getAlignmentLength();
      result = 8;
      times = 1;
    }};

    assertEquals(100.0 * 19.0/(5*8), objective.compute(solution, solution.decodeToMatrix()), EPSILON) ;
  }
}