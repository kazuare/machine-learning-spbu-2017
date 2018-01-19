public class StepData {
	double longitude;
	double latitude;
	long request_ts;
	long trans_ts;
	char label;
	
	boolean isAlreadyDetermined = false;
	
	StepData(double longitude, double latitude, long request_ts, long trans_ts,	char label){
		this.longitude = longitude;
		this.latitude = latitude;
		this.request_ts = request_ts;
		this.trans_ts = trans_ts;
		this.label = label;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof StepData))
			return false;
		StepData other = (StepData)o;
		return this.longitude == other.longitude &&	this.latitude == other.latitude;
	}

	@Override
	public String toString(){
		return longitude + " " + latitude + " " + request_ts + " " + trans_ts + " " + label;
	}
}
