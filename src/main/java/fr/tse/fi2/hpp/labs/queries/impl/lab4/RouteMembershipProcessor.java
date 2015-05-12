package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor{

	static ArrayList<DebsRecord> tabroute = new ArrayList<DebsRecord> () ;
	private int count = 0;
	private static DebsRecord recTest;
	
	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		tabroute.add(record);
		count++;
		if(count==15){
			recTest = record;
		}

	}

	public static DebsRecord getRec(){ return recTest; }
	public static boolean CheckRoute(DebsRecord rec){
		
		for(int i=0;i<tabroute.size();i++)
		{
			if (rec.getPickup_longitude()==tabroute.get(i).getPickup_longitude() 
					&& rec.getPickup_latitude()==tabroute.get(i).getPickup_latitude() 
					&& rec.getDropoff_longitude()==tabroute.get(i).getDropoff_longitude() 
					&& rec.getDropoff_latitude()==tabroute.get(i).getDropoff_latitude() 
					&& rec.getHack_license()==tabroute.get(i).getHack_license() )
			{
				return true;
			}
		}
		return false;
	}
}
