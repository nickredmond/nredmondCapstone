package featureExtraction;

public enum FeatureType {
	T_JUNCTION{
		@Override
		public String toString(){
			return "T";
		}
	},
	END_POINT{
		@Override
		public String toString(){
			return "E";
		}
	};
}
