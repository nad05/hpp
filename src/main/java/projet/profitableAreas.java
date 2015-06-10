package projet;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;

public class profitableAreas {
	
	private int nbTaxisVides;
	private ArrayList<DebsRecord> records;
	private float profitability;	
	private String cell;
	protected ListeMediane mediane;

	public profitableAreas() {
		setNbTaxisVides(0);
		setRec(new ArrayList<DebsRecord>());
		mediane = new ListeMediane();
		
	}
	
	public void add(DebsRecord rec) {
		if(rec.getFare_amount() + rec.getTip_amount() > 0) {
			records.add(rec);
		}
	}
	
	public void calculprofitability(){
		if(nbTaxisVides>0){
			profitability = mediane.getMediane()/nbTaxisVides;
		}
		else{
			profitability=-1;
		}
	}
	
	public int getNbTaxisVides() {
		return nbTaxisVides;
	}

	public void setNbTaxisVides(int nbTaxisVides) {
		this.nbTaxisVides = nbTaxisVides;
	}

	public ArrayList<DebsRecord> getRec() {
		return records;
	}

	public void setRec(ArrayList<DebsRecord> rec) {
		this.records = rec;
	}

	public float getProfitability() {
		return profitability;
	}

	public void setProfitability(float profitability) {
		this.profitability = profitability;
	}
	
	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		result = prime * result + ((mediane == null) ? 0 : mediane.hashCode());
		result = prime * result + nbTaxisVides;
		result = prime * result + Float.floatToIntBits(profitability);
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		profitableAreas other = (profitableAreas) obj;
		if (cell == null) {
			if (other.cell != null)
				return false;
		} else if (!cell.equals(other.cell))
			return false;
		if (mediane == null) {
			if (other.mediane != null)
				return false;
		} else if (!mediane.equals(other.mediane))
			return false;
		if (nbTaxisVides != other.nbTaxisVides)
			return false;
		if (Float.floatToIntBits(profitability) != Float
				.floatToIntBits(other.profitability))
			return false;
		if (records == null) {
			if (other.records != null)
				return false;
		} else if (!records.equals(other.records))
			return false;
		return true;
	}

	
}