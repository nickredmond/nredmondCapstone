package featureExtraction;

import java.awt.Point;

public enum VectorDirection {
	TOP_RIGHT{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((row > 0 && col < skeletonValues[0].length - 1) ? new Point(col+1, row-1) : new Point(-1, -1));
		}
	},
	RIGHT{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((col < skeletonValues[0].length - 1) ? new Point(col+1, row) : new Point(-1, -1));
		}
	},
	BOTTOM_RIGHT{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((row < skeletonValues.length - 1 && col < skeletonValues[0].length - 1) ? new Point(col+1, row+1) : new Point(-1, -1));
		}
	},
	BOTTOM{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((row < skeletonValues.length - 1) ? new Point(col, row+1) : new Point(-1, -1));
		}
	},
	BOTTOM_LEFT{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((row < skeletonValues.length - 1 && col > 0) ? new Point(col-1, row+1) : new Point(-1, -1));
		}
	},
	LEFT{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((col > 0) ? new Point(col-1, row) : new Point(-1, -1));
		}
	},
	TOP_LEFT{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((row > 0 && col > 0) ? new Point(col-1, row-1) : new Point(-1, -1));
		}
	},
	TOP{
		@Override
		public Point getNextPixelPoint(int row, int col, int[][] skeletonValues){
			return ((row > 0) ? new Point(col, row-1) : new Point(-1, -1));
		}
	};
	
	public abstract Point getNextPixelPoint(int row, int col, int[][] skeletonValues);
}
