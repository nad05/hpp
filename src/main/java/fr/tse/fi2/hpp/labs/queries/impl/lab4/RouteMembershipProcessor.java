package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor{

	ArrayList<DebsRecord> tabroute = new ArrayList<DebsRecord> () ;
	
	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		tabroute.add(record);
    		
	}
	
	public int CheckRoute(float plong,float plat,float dlong,float dlat,String lic){
		
		for(int i=0;i<tabroute.size();i++)
		{
			if (plong==tabroute.get(i).getPickup_longitude() 
					&& plat==tabroute.get(i).getPickup_latitude() 
					&& dlong==tabroute.get(i).getDropoff_longitude() 
					&& dlat==tabroute.get(i).getDropoff_latitude() 
					&& lic==tabroute.get(i).getHack_license() )
			{
				return i;
			}
		}
		return -1;
	}
}
