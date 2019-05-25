public enum Direction {
	North {
		@Override
		public int di() {
			return -1;
		}

		@Override
		public int dj() {
			return 0;
		}

		@Override
		public Direction opposite() {
			return South;
		}

		@Override
		public String toString() {
			return "N";
		}
	}, East {
		@Override
		public int di() {
			return 0;
		}

		@Override
		public int dj() {
			return +1;
		}

		@Override
		public Direction opposite() {
			return West;
		}

		@Override
		public String toString() {
			return "E";
		}
	}, South {
		@Override
		public int di() {
			return +1;
		}

		@Override
		public int dj() {
			return 0;
		}

		@Override
		public Direction opposite() {
			return North;
		}

		@Override
		public String toString() {
			return "S";
		}
	}, West {
		@Override
		public int di() {
			return 0;
		}

		@Override
		public int dj() {
			return -1;
		}

		@Override
		public Direction opposite() {
			return East;
		}

		@Override
		public String toString() {
			return "W";
		}
	};

	public abstract int di();
	public abstract int dj();

	public abstract Direction opposite();
}

