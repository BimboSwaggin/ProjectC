public class MappedBackgroundAnimation implements Animation {

	private int universeCount = 0;
	private Universe current = null;
	
	public Universe getNextUniverse() {

		universeCount++;
		
		if (universeCount == 1) {
			return new MappedBackgroundUniverse();
		}
		else {
			this.current = null;
		}
		return this.current;

	}

	public Universe getCurrentUniverse() {
		return this.current;
	}
	
}
