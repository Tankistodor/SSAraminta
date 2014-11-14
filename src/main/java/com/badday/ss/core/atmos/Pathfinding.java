package com.badday.ss.core.atmos;


import com.badday.ss.SSConfig;
import com.badday.ss.api.SSAPI;
import com.badday.ss.core.utils.BlockVec3;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

@Deprecated
public class Pathfinding {
  public static int PATH_ITERATIONS = 128;

  private World world;
  private BlockVec3 start;
  private BlockVec3 end;
  private float maxDistance = -1;
  private float sqrMaxDistance = SSConfig.ssPathfinderMaxDistance*SSConfig.ssPathfinderMaxDistance; // TODO: Make right later
  private double maxDistanceToEnd = 0;

  private HashMap<BlockVec3, Node> openList = new HashMap<BlockVec3, Pathfinding.Node>();
  private HashMap<BlockVec3, Node> closedList = new HashMap<BlockVec3, Pathfinding.Node>();

  private Node nextIteration;

  private LinkedList<BlockVec3> result;

  private boolean endReached = false;

  public Pathfinding(World iWorld, BlockVec3 iStart, BlockVec3 iEnd) {
    world = iWorld;
    start = iStart;
    end = iEnd;

    Node startNode = new Node();
    startNode.parent = null;
    startNode.movementCost = 0;
    startNode.destinationCost = distance(start, end);
    startNode.totalWeight = startNode.movementCost + startNode.destinationCost;
    startNode.index = iStart;
    openList.put(start, startNode);
    nextIteration = startNode;
  }

  public int find_distance() {

    return -1;
  }

  public void iterate() {
    iterate(PATH_ITERATIONS);
  }

  public void iterate(int itNumber) {
    for (int i = 0; i < itNumber; ++i) {
      if (nextIteration == null) {
        return;
      }

      if (endReached) {
        result = new LinkedList<BlockVec3>();

        while (nextIteration != null) {
          result.addFirst(nextIteration.index);
          nextIteration = nextIteration.parent;
        }

        return;
      } else {
        nextIteration = iterate(nextIteration);
      }
    }
  }

  public boolean isDone() {
    return nextIteration == null;
  }

  public LinkedList<BlockVec3> getResult() {
    if (result != null) {
      return result;
    } else {
      return new LinkedList<BlockVec3>();
    }
  }

  private Node iterate(Node from) {
    openList.remove(from.index);
    closedList.put(from.index, from);

    ArrayList<Node> nodes = new ArrayList<Node>();
    byte[][][] resultMoves = movements(from);

    for (int dx = -1; dx <= +1; ++dx) {
      for (int dy = -1; dy <= +1; ++dy) {
        for (int dz = -1; dz <= +1; ++dz) {
          if (resultMoves[dx + 1][dy + 1][dz + 1] == 0) {
            continue;
          }

          int x = from.index.x + dx;
          int y = from.index.y + dy;
          int z = from.index.z + dz;

          Node nextNode = new Node();
          nextNode.parent = from;
          nextNode.index = new BlockVec3(x, y, z);

          if (resultMoves[dx + 1][dy + 1][dz + 1] == 2) {
            endReached = true;
            return nextNode;
          }

          nextNode.movementCost = from.movementCost + distance(nextNode.index, from.index);

          if (end != null) {
            nextNode.destinationCost = distance(nextNode.index, end);
          } else {
            nextNode.destinationCost = 0;
          }

          nextNode.totalWeight = nextNode.movementCost + nextNode.destinationCost;

          if (closedList.containsKey(nextNode.index)) {
            continue;
          } else if (openList.containsKey(nextNode.index)) {
            Node tentative = openList.get(nextNode.index);

            if (tentative.movementCost < nextNode.movementCost) {
              nextNode = tentative;
            } else {
              openList.put(nextNode.index, nextNode);
            }
          } else {
            openList.put(nextNode.index, nextNode);
          }

          nodes.add(nextNode);
        }
      }
    }

    nodes.addAll(openList.values());

    return findSmallerWeight(nodes);
  }

  private Node findSmallerWeight(Collection<Node> collection) {
    Node found = null;

    for (Node n : collection) {
      if (found == null) {
        found = n;
      } else if (n.totalWeight < found.totalWeight) {
        found = n;
      }
    }

    return found;
  }

  private static class Node {
    public Node parent;
    public double movementCost;
    public double destinationCost;
    public double totalWeight;
    public BlockVec3 index;
  }

  private static double distance(BlockVec3 i1, BlockVec3 i2) {
    double dx = (double) i1.x - (double) i2.x;
    double dy = (double) i1.y - (double) i2.y;
    double dz = (double) i1.z - (double) i2.z;

    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  private boolean endReached(int x, int y, int z) {
    if (maxDistanceToEnd == 0) {
      return end.x == x && end.y == y && end.z == z;
    } else {
      return SSAPI.isSoftBlock(world, x, y, z)
          && distance(new BlockVec3(x, y, z), end) <= maxDistanceToEnd;
    }
  }

  private byte[][][] movements(Node from) {
    byte[][][] resultMoves = new byte[3][3][3];

    for (int dx = -1; dx <= +1; ++dx) {
      for (int dy = -1; dy <= +1; ++dy) {
        for (int dz = -1; dz <= +1; ++dz) {
          int x = from.index.x + dx;
          int y = from.index.y + dy;
          int z = from.index.z + dz;

          if (endReached(x, y, z)) {
            resultMoves[dx + 1][dy + 1][dz + 1] = 2;
          } else if (!SSAPI.isSoftBlock(world, x, y, z)) {
            resultMoves[dx + 1][dy + 1][dz + 1] = 0;
          } else {
            resultMoves[dx + 1][dy + 1][dz + 1] = 1;
          }
        }
      }
    }

    resultMoves[1][1][1] = 0;

    if (resultMoves[0][1][1] == 0) {
      for (int i = 0; i <= 2; ++i) {
        for (int j = 0; j <= 2; ++j) {
          resultMoves[0][i][j] = 0;
        }
      }
    }

    if (resultMoves[2][1][1] == 0) {
      for (int i = 0; i <= 2; ++i) {
        for (int j = 0; j <= 2; ++j) {
          resultMoves[2][i][j] = 0;
        }
      }
    }

    if (resultMoves[1][0][1] == 0) {
      for (int i = 0; i <= 2; ++i) {
        for (int j = 0; j <= 2; ++j) {
          resultMoves[i][0][j] = 0;
        }
      }
    }

    if (resultMoves[1][2][1] == 0) {
      for (int i = 0; i <= 2; ++i) {
        for (int j = 0; j <= 2; ++j) {
          resultMoves[i][2][j] = 0;
        }
      }
    }

    if (resultMoves[1][1][0] == 0) {
      for (int i = 0; i <= 2; ++i) {
        for (int j = 0; j <= 2; ++j) {
          resultMoves[i][j][0] = 0;
        }
      }
    }

    if (resultMoves[1][1][2] == 0) {
      for (int i = 0; i <= 2; ++i) {
        for (int j = 0; j <= 2; ++j) {
          resultMoves[i][j][2] = 0;
        }
      }
    }

    if (resultMoves[0][0][1] == 0) {
      resultMoves[0][0][0] = 0;
      resultMoves[0][0][2] = 0;
    }

    if (resultMoves[0][2][1] == 0) {
      resultMoves[0][2][0] = 0;
      resultMoves[0][2][2] = 0;
    }

    if (resultMoves[2][0][1] == 0) {
      resultMoves[2][0][0] = 0;
      resultMoves[2][0][2] = 0;
    }

    if (resultMoves[2][2][1] == 0) {
      resultMoves[2][2][0] = 0;
      resultMoves[2][2][2] = 0;
    }

    if (resultMoves[0][1][0] == 0) {
      resultMoves[0][0][0] = 0;
      resultMoves[0][2][0] = 0;
    }

    if (resultMoves[0][1][2] == 0) {
      resultMoves[0][0][2] = 0;
      resultMoves[0][2][2] = 0;
    }

    if (resultMoves[2][1][0] == 0) {
      resultMoves[2][0][0] = 0;
      resultMoves[2][2][0] = 0;
    }

    if (resultMoves[2][1][2] == 0) {
      resultMoves[2][0][2] = 0;
      resultMoves[2][2][2] = 0;
    }

    if (resultMoves[1][0][0] == 0) {
      resultMoves[0][0][0] = 0;
      resultMoves[2][0][0] = 0;
    }

    if (resultMoves[1][0][2] == 0) {
      resultMoves[0][0][2] = 0;
      resultMoves[2][0][2] = 0;
    }

    if (resultMoves[1][2][0] == 0) {
      resultMoves[0][2][0] = 0;
      resultMoves[2][2][0] = 0;
    }

    if (resultMoves[1][2][2] == 0) {
      resultMoves[0][2][2] = 0;
      resultMoves[2][2][2] = 0;
    }


    if (maxDistance != -1) {
      for (int dx = -1; dx <= +1; ++dx) {
        for (int dy = -1; dy <= +1; ++dy) {
          for (int dz = -1; dz <= +1; ++dz) {
            int x = from.index.x + dx;
            int y = from.index.y + dy;
            int z = from.index.z + dz;

            float distX = x - start.x;
            float distY = y - start.y;
            float distZ = z - start.z;
            float sqrDist = distX * distX + distY * distY + distZ * distZ;

            if (sqrDist > sqrMaxDistance) {
              resultMoves[dx + 1][dy + 1][dz + 1] = 0;
            }
          }
        }
      }
    }

    return resultMoves;
  }

}
