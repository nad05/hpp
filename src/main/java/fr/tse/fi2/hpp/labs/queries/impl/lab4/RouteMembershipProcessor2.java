package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor2 extends AbstractQueryProcessor{

	public static BitSet listeHashRoute = new BitSet(14378);
	
	public RouteMembershipProcessor2(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {

		String result = getRoute(record);

		for(int i =0 ; i<10 ; i++){
			String resultHash = result + i;

			int index =SHA3Util.digest(resultHash,512,14378);
			listeHashRoute.set( index );
		}
	}

	private static String getRoute(DebsRecord record) {
		// TODO Auto-generated method stub
		String result = null;
		result += record.getPickup_longitude();
		result += record.getPickup_latitude();
		result += record.getDropoff_longitude();
		result += record.getDropoff_latitude();
		result += record.getHack_license();
		
		return result;
	}

	public static boolean CheckRoute(DebsRecord rec){
		
		String result = getRoute(rec);
		
		for(int i =0 ; i<10 ; i++){
			String resultHash = result + i;
			
			int index = SHA3Util.digest(resultHash,512,14378);
			
			if(listeHashRoute.get(index) == false){
				return false;	
			}
		}
		return true;
	}
}
