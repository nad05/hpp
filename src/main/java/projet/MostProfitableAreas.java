package projet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.text.TabableView;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MostProfitableAreas extends AbstractQueryProcessor {

	long currentTime ;
	final long MINUTES_FENETRES = 15 * 60 * 1000;
	LinkedList<DebsRecord> area15min ;
	LinkedList<DebsRecord> fenetre15min;
	LinkedList<DebsRecord> fenetreDrop30min;
	LinkedList<DebsRecord> fenetrePick30min;
	HashMap<String, ListeMediane> profitArea;
	ArrayList<profitableAreas> array10most;
	public MostProfitableAreas(QueryProcessorMeasure measure) {
		super(measure);
		currentTime = 0;
		area15min= new LinkedList<DebsRecord>();
		fenetre15min = new LinkedList<DebsRecord>();
		array10most = new ArrayList<profitableAreas>();
		profitArea = new HashMap<String, ListeMediane>();
		fenetreDrop30min = new LinkedList<DebsRecord>();
		fenetrePick30min = new LinkedList<DebsRecord>();
	}

	@Override
	protected double process(DebsRecord record) {
		currentTime=record.getDropoff_datetime();

		
		// informations concernant le trajet
				String caseDepart  = convertToCell(record.getPickup_latitude(), record.getPickup_longitude());
				float fare = record.getFare_amount();
				float tip  = record.getTip_amount();
				
				// si la case n'est pas dans la grille, aucun calcul n'est effectue
				if (caseDepart.equals(""))
				{
					return 0;
				}
				
				
				// Ajout ou mise à jour du profit de la case dans la hashmap
				// on ne doit prendre en compte que les trajets sans erreurs 
				// (donc fare et tip doivent etre positifs)
				if (fare >= 0 && tip >= 0)
				{
					// Ajout de la course dans la fenetre des 15 min
					fenetre15min.add(record);
					
					if (profitArea.containsKey(caseDepart))
					{
						profitArea.get(caseDepart).ajouteGain(fare + tip);
						
					}
					
					else
					{
						ListeMediane tmp = new ListeMediane();
						tmp.ajouteGain(fare + tip);
						profitArea.put(caseDepart, tmp);
					}
				}
				
				
				// On supprime les gains des courses qui datent de plus de 15 min
				removeEarnings15min(currentTime - MINUTES_FENETRES);
				
				for (String cell : profitArea.keySet())
				{
					System.out.println(profitArea.get(cell).values.size());
					String profit = String.valueOf(profitArea.get(cell).getMediane());
					System.out.println("Area : " + cell + "\tProfit : " + profit);
				}
				
				System.out.println("-------------");
				
			

		//Récupération des zones dans les 15 dernière minutes et calcul du nombre de trajet
				ArrayList<profitableAreas> tabArea = new ArrayList<profitableAreas>();
				
				area15min.add(record);
				for(int i=0;i<area15min.size();i++){
					if ((currentTime- area15min.get(i).getPickup_datetime()) >MINUTES_FENETRES){
						area15min.remove(i);
					}
				}
				
				for(int i=0;i<area15min.size();i++){
					profitableAreas a = new profitableAreas();
					a.setCell(convertToCell(area15min.get(i).getPickup_latitude() , area15min.get(i).getPickup_longitude()));
					boolean find = false;
					for(int j=0;j<tabArea.size();j++){
						if(a.getCell().equals(tabArea.get(j).getCell())){
							tabArea.get(j).mediane.ajouteGain(fare + tip);
							tabArea.get(j).mediane.getMediane();
							tabArea.get(j).calculprofitability();
							find = true;
						}
					}
					if(!find){
						a.setNbTaxisVides(0);
						a.mediane.ajouteGain(fare + tip);
						a.mediane.getMediane();
						a.calculprofitability();
						tabArea.add(a);
						
					}
					
					
				}
			
		//Calcul du nombre de taxi vide
				fenetrePick30min.add(record);
				for(int i=0;i<fenetrePick30min.size();i++){
					if ((currentTime - fenetrePick30min.get(i).getPickup_datetime()) >(2*MINUTES_FENETRES)){
						fenetrePick30min.remove(i);
						
					}
				}
				
				
				for(int i=0;i<fenetreDrop30min.size();i++){
					if ((currentTime- fenetreDrop30min.get(i).getPickup_datetime()) >(2*MINUTES_FENETRES)){
						fenetreDrop30min.remove(i);
						
					}
				}
				fenetreDrop30min.add(record);
				
				for(int i =0;i<fenetreDrop30min.size();i++){
					boolean find = false;
					for(int j=0;j<fenetrePick30min.size();j++){
						if(fenetreDrop30min.get(i).getHack_license().equals(fenetrePick30min.get(j).getHack_license())){
							if(fenetrePick30min.get(j).getPickup_datetime()-fenetreDrop30min.get(i).getDropoff_datetime()>0){
								find = true;
							}
						}
					}
					
					if(!find){
						String dropArea = convertToCell(fenetreDrop30min.get(i).getPickup_latitude() , fenetreDrop30min.get(i).getPickup_longitude());
						for(int k =0;k<tabArea.size();k++){
							if(dropArea.equals(tabArea.get(k).getCell())){
								tabArea.get(k).setNbTaxisVides(tabArea.get(k).getNbTaxisVides()+1);
							}
						}
					}
				}
				
				
				for(int i = 0;i<tabArea.size();i++){
					tabArea.get(i).calculprofitability();
				}
				
				
				System.out.println(fenetrePick30min.size());
				for(int i=0;i<tabArea.size();i++){
					System.out.println("cell : " + tabArea.get(i).getCell() + 
										" Taxi Empty : " + tabArea.get(i).getNbTaxisVides() + " Median profit : " + tabArea.get(i).mediane.getMediane() + " Profitability : "  + tabArea.get(i).getProfitability());
				}
		
				return 0;
	}

	/**
	 * Retourne la case a laquelle appartient la coordonnee (latitude,longitude).
	 * @param latitude
	 * @param longitude
	 * @return La case sous la forme d'un entier. Les coordonnees de la case sont comprises entre
	 * 1 et 600. On retourne (cellX * 1000 + cellY)
	 */
	private String convertToCell(float latitude, float longitude)
	{
		// latitude = ordonnee; longitude = abscisse
		
		// longueur et largeur de chaque case
		double step_abscisse = 0.005986/2;
		double step_ordonnee = 0.004491556/2;
		
		// origine de la grille
		double starting_abscisse = -74.913585;
		double starting_ordonnee = 41.474937;
		
		// nombre de cases separant le point en parametre de l'origine de la grille
		double cellX = (longitude - starting_abscisse) / step_abscisse;
		double cellY = (starting_ordonnee - latitude ) / step_ordonnee;
		
		// on ne garde que la partie entiere du nombre de cases
		Integer X = (int) (Math.round(cellX) + 1);
		Integer Y = (int) (Math.round(cellY) + 1);
		String result = X.toString() + "." + Y.toString();
		return result;
	}
	
	private void removeEarnings15min(long time)
	{
		while (true)
		{
			DebsRecord elem = fenetre15min.getFirst();
			
			// on enleve le gain des courses qui sont en dehors de la fenetre de 15 min
			// et on enleve ces debsRecord de la fenetre
			if (elem.getDropoff_datetime() < time)
			{
				// trouve la case de depart de cette course
				String cell = convertToCell(elem.getPickup_latitude(), elem.getPickup_longitude());
				
				// on est sur que le gain peut etre enleve car si le debsRecord est dans la 
				// fenetre, alors on a ajoute son gain
				profitArea.get(cell).supprimeGain(elem.getFare_amount() + elem.getTip_amount());
				
				// supprime de la fenetre
				fenetre15min.removeFirst();
			}
			else 
			{
				return;
			}
		}
	}
}
