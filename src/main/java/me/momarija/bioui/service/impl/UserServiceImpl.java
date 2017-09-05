package me.momarija.bioui.service.impl;

import me.momarija.bioui.domains.Chantier;
import me.momarija.bioui.domains.Donnee;
import me.momarija.bioui.domains.Engin;
import me.momarija.bioui.domains.Statistic;
import me.momarija.bioui.repos.ChantierRepo;
import me.momarija.bioui.repos.DonneeRepo;
import me.momarija.bioui.repos.EnginRepo;
import me.momarija.bioui.service.UserService;
import me.momarija.bioui.service.algo.DateUtility;
import me.momarija.bioui.service.algo.TraitementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ChantierRepo chantierRepo;

	@Autowired
	private EnginRepo enginRepo;

	@Autowired
	private DonneeRepo donneeRepo;

	@Autowired
	private TraitementService traitementService;

	private DateUtility dateUtility = new DateUtility();

    @Override
    public List<Map<String, Object>> getChantiersRendementWeek(){
        List<Map<String,Object>> list = new ArrayList<>();
        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 518400*1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);
        Map<String,Object> mapChantierRendement = new HashMap<>();

        for (Chantier c:chantierRepo.findAll()) {
            mapChantierRendement.put("chantier",c);
            mapChantierRendement.put("rendement",getChantierRendement(c.getId(),statistic));
            list.add(mapChantierRendement);
            mapChantierRendement=new HashMap<>();
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> getChantiersRendementTwoWeek(){
        List<Map<String,Object>> list = new ArrayList<>();
        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000 ;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);
        Map<String,Object> mapChantierRendement = new HashMap<>();

        for (Chantier c:chantierRepo.findAll()) {
            mapChantierRendement.put("chantier",c);
            mapChantierRendement.put("rendement",getChantierRendement(c.getId(),statistic));
            list.add(mapChantierRendement);
            mapChantierRendement=new HashMap<>();
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> getChantiersRendementMonth(){
        List<Map<String,Object>> list = new ArrayList<>();
        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000 - 604800*1000 - 302400 * 1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);
        Map<String,Object> mapChantierRendement = new HashMap<>();

        for (Chantier c:chantierRepo.findAll()) {
            mapChantierRendement.put("chantier",c);
            mapChantierRendement.put("rendement",getChantierRendement(c.getId(),statistic));
            list.add(mapChantierRendement);
            mapChantierRendement=new HashMap<>();
        }

        return list;
    }

    @Override
	public List<Map<String, Object>> getChantiersRendement(Statistic statistic) {
		List<Map<String,Object>> list = new ArrayList<>();

		Map<String,Object> mapChantierRendement = new HashMap<>();

		for (Chantier c:chantierRepo.findAll()) {
			mapChantierRendement.put("chantier",c);
			mapChantierRendement.put("rendement",String.format("%.2f", getChantierRendement(c.getId(),statistic)));
			list.add(mapChantierRendement);
			mapChantierRendement=new HashMap<>();
		}

		return list;
	}

	@Override
	public List<Map<String, String>> getChantierStatistics(int chantierId, Statistic statistic) {
		List<Map<String,String>> list = new ArrayList<>();
		int i;
		Date d1,d2,d3;
		long y;
		SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

		int nbJours = statistic.calculNbJours();

		String dayFrom =stf.format(statistic.getDayFrom());
		String hourFrom = stimef.format(statistic.getHourFrom());
		String hourTo = stimef.format(statistic.getHourTo());

		Map<String ,Integer> map ;
		Map<String ,String> map2 ;
		double p_percent;long z;

		int a=0,r=0,p=0;float perc =0.2f;
		for(i=0;i<=nbJours;i++){
			d1= new Date(dayFrom+" "+hourFrom);
			d3= new Date(dayFrom+" "+hourTo);
			y = d1.getTime()+24*3600*1000;
			d2 = new Date(y);
			z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
			map2 = new HashMap<>();
			System.out.println(chantierId+" ***********");
			List<Engin> listEngin = getEnginList(chantierId);
			for (Engin engin :listEngin) {

				map = doWork(engin,d1,d3);
				int arret = (int)z/1000 - map.get("production") - map.get("ralenti");
				if(arret < 0){
					map.put("ralenti",map.get("ralenti")+arret);
					arret = 0 ;
				}
				if(map.get("ralenti")<0) map.put("ralenti",0);

				p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

				a+=arret;
				r+=map.get("ralenti");
				p+=map.get("production");
				perc+=p_percent;

			}

			map2.put("production",dateUtility.convertToDate(p/listEngin.size()));
			map2.put("ralenti",dateUtility.convertToDate(r/listEngin.size()));
			map2.put("arret", dateUtility.convertToDate(a/listEngin.size()));
			map2.put("date", dayFrom);
			map2.put("pause",statistic.getNbHourRepos()+" h");
			map2.put("rendement",String.format("%.2f",perc/listEngin.size() )+" %" );
			list.add(map2);
			a=0;r=0;p=0;perc=0.2f;
			map2= new HashMap<>();
			dayFrom = stf.format(d2);

		}

		return list;
	}

    @Override
    public List<Map<String, String>> getChantierStatisticsWeek(int chantierId) {
        List<Map<String,String>> list = new ArrayList<>();
        int i;
        Date d1,d2,d3 ;
        long y;
        SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 518400*1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,20);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);


        int nbJours = statistic.calculNbJours();

        String dayFrom =stf.format(statistic.getDayFrom());
        String hourFrom = stimef.format(statistic.getHourFrom());
        String hourTo = stimef.format(statistic.getHourTo());

        Map<String ,Integer> map ;
        Map<String ,String> map2 ;
        double p_percent;long z;
        int a=0,r=0,p=0;float perc =0.0f;
        for(i=0;i<=nbJours;i++){
            d1= new Date(dayFrom+" "+hourFrom);
            d3= new Date(dayFrom+" "+hourTo);
            y = d1.getTime()+24*3600*1000;
            d2 = new Date(y);
            z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
            map2 = new HashMap<>();
            List<Engin> listEngin = getEnginList(chantierId);
            for (Engin engin :listEngin) {
                map = doWork(engin,d1,d3);
                int arret = (int)z/1000 - map.get("production") - map.get("ralenti");
                if(arret < 0){
                    map.put("ralenti",map.get("ralenti")+arret);
                    arret = 0 ;
                }
                if(map.get("ralenti")<0) map.put("ralenti",0);

                p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

                a+=arret;
                r+=map.get("ralenti");
                p+=map.get("production");
                perc+=p_percent;

            }

            map2.put("production",dateUtility.convertToDate(p/listEngin.size()));
            map2.put("ralenti",dateUtility.convertToDate(r/listEngin.size()));
            map2.put("arret", dateUtility.convertToDate(a/listEngin.size()));
            map2.put("date", dayFrom);
            map2.put("pause",statistic.getNbHourRepos()+" h");
            map2.put("rendement",String.format("%.2f",perc/listEngin.size() )+" %" );
            list.add(map2);
            a=0;r=0;p=0;perc=0.0f;
            map2= new HashMap<>();
            dayFrom = stf.format(d2);

        }

        return list;
    }

    @Override
    public List<Map<String, String>> getChantierStatisticsTwoWeek(int chantierId) {
        List<Map<String,String>> list = new ArrayList<>();
        int i;
        Date d1,d2,d3 ;
        long y;
        SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1209600*1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,20);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);


        int nbJours = statistic.calculNbJours();

        String dayFrom =stf.format(statistic.getDayFrom());
        String hourFrom = stimef.format(statistic.getHourFrom());
        String hourTo = stimef.format(statistic.getHourTo());

        Map<String ,Integer> map ;
        Map<String ,String> map2 ;
        double p_percent;long z;
        int a=0,r=0,p=0;float perc =0.0f;
        for(i=0;i<=nbJours;i++){
            d1= new Date(dayFrom+" "+hourFrom);
            d3= new Date(dayFrom+" "+hourTo);
            y = d1.getTime()+24*3600*1000;
            d2 = new Date(y);
            z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
            map2 = new HashMap<>();
            List<Engin> listEngin = getEnginList(chantierId);
            for (Engin engin :listEngin) {
                map = doWork(engin,d1,d3);
                int arret = (int)z/1000 - map.get("production") - map.get("ralenti");
                if(arret < 0){
                    map.put("ralenti",map.get("ralenti")+arret);
                    arret = 0 ;
                }
                if(map.get("ralenti")<0) map.put("ralenti",0);

                p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

                a+=arret;
                r+=map.get("ralenti");
                p+=map.get("production");
                perc+=p_percent;

            }

            map2.put("production",dateUtility.convertToDate(p/listEngin.size()));
            map2.put("ralenti",dateUtility.convertToDate(r/listEngin.size()));
            map2.put("arret", dateUtility.convertToDate(a/listEngin.size()));
            map2.put("date", dayFrom);
            map2.put("pause",statistic.getNbHourRepos()+" h");
            map2.put("rendement",String.format("%.2f",perc/listEngin.size() )+" %" );
            list.add(map2);
            a=0;r=0;p=0;perc=0.0f;
            map2= new HashMap<>();
            dayFrom = stf.format(d2);

        }

        return list;
    }

    @Override
    public List<Map<String, String>> getChantierStatisticsMonth(int chantierId) {
        List<Map<String,String>> list = new ArrayList<>();
        int i;
        Date d1,d2,d3 ;
        long y;
        SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000 - 604800*1000 - 302400 * 1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,20);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);

        System.out.println(statistic.getDayFrom());
        System.out.println(statistic.getDayTo());


        int nbJours = statistic.calculNbJours();

        String dayFrom =stf.format(statistic.getDayFrom());
        String hourFrom = stimef.format(statistic.getHourFrom());
        String hourTo = stimef.format(statistic.getHourTo());

        Map<String ,Integer> map ;
        Map<String ,String> map2 ;
        double p_percent;long z;
        int a=0,r=0,p=0;float perc =0.0f;
        for(i=0;i<=nbJours;i++){
            d1= new Date(dayFrom+" "+hourFrom);
            d3= new Date(dayFrom+" "+hourTo);
            y = d1.getTime()+24*3600*1000;
            d2 = new Date(y);
            z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
            map2 = new HashMap<>();
            List<Engin> listEngin = getEnginList(chantierId);
            for (Engin engin :listEngin) {
                map = doWork(engin,d1,d3);
                int arret = (int)z/1000 - map.get("production") - map.get("ralenti");
                if(arret < 0){
                    map.put("ralenti",map.get("ralenti")+arret);
                    arret = 0 ;
                }
                if(map.get("ralenti")<0) map.put("ralenti",0);

                p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

                a+=arret;
                r+=map.get("ralenti");
                p+=map.get("production");
                perc+=p_percent;

            }

            map2.put("production",dateUtility.convertToDate(p/listEngin.size()));
            map2.put("ralenti",dateUtility.convertToDate(r/listEngin.size()));
            map2.put("arret", dateUtility.convertToDate(a/listEngin.size()));
            map2.put("date", dayFrom);
            map2.put("pause",statistic.getNbHourRepos()+" h");
            map2.put("rendement",String.format("%.2f",perc/listEngin.size() )+" %" );
            list.add(map2);
            a=0;r=0;p=0;perc=0.0f;
            map2= new HashMap<>();
            dayFrom = stf.format(d2);

        }

        return list;
    }

    @Override
	public List<Map<String, Object>> getEnginsRendementWeek(int chantierId) {
		List<Map<String,Object>> list = new ArrayList<>();
		Statistic statistic = new Statistic();
		Date d_to = new Date();
		long week_Ms = d_to.getTime() - 518400*1000;
		Date d_from = new Date(week_Ms);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,6);
		cal.set(Calendar.MINUTE,30);
		cal.set(Calendar.SECOND,0);

		Date time_from = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY,19);
		cal.set(Calendar.MINUTE,00);
		cal.set(Calendar.SECOND,0);
		Date time_to = cal.getTime();

		statistic.setDayTo(d_to);
		statistic.setDayFrom(d_from);
		statistic.setHourFrom(time_from);
		statistic.setHourTo(time_to);
		statistic.setNbHourRepos(1);

		Map<String,Object> mapEnginRendement = new HashMap<>();
		for (Engin e:getEnginList(chantierId)) {
			mapEnginRendement.put("engin",e);
			mapEnginRendement.put("rendement",getEnginRendement(e.getId(),statistic));
			list.add(mapEnginRendement);
			mapEnginRendement=new HashMap<>();
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getEnginsRendement(int chantierId, Statistic statistic) {
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> mapEnginRendement = new HashMap<>();
		for (Engin e:getEnginList(chantierId)) {
			mapEnginRendement.put("engin",e);
			mapEnginRendement.put("rendement",String.format("%.2f",getEnginRendement(e.getId(),statistic)) );
			list.add(mapEnginRendement);
			mapEnginRendement=new HashMap<>();
		}
		return list;
	}

    @Override
    public List<Map<String, Object>> getEnginsRendementTwoWeek(int chantierId) {
        List<Map<String,Object>> list = new ArrayList<>();
        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);

        Map<String,Object> mapEnginRendement = new HashMap<>();
        for (Engin e:getEnginList(chantierId)) {
            mapEnginRendement.put("engin",e);
            mapEnginRendement.put("rendement",getEnginRendement(e.getId(),statistic));
            list.add(mapEnginRendement);
            mapEnginRendement=new HashMap<>();
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getEnginsRendementMonth(int chantierId) {
        List<Map<String,Object>> list = new ArrayList<>();
        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000 - 604800*1000 - 302400 * 1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);

        Map<String,Object> mapEnginRendement = new HashMap<>();
        for (Engin e:getEnginList(chantierId)) {
            mapEnginRendement.put("engin",e);
            mapEnginRendement.put("rendement",getEnginRendement(e.getId(),statistic));
            list.add(mapEnginRendement);
            mapEnginRendement=new HashMap<>();
        }
        return list;
    }

    @Override
    public List<Map<String, String>> getEnginStatisticsWeek(int enginId) {

        List<Map<String,String>> list = new ArrayList<>();
        int i;Date d1,d2,d3 ;
        long y;
        Engin engin = enginRepo.findOne(enginId);

        SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 518400*1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);
        int nbJours = statistic.calculNbJours();

        String dayFrom =stf.format(statistic.getDayFrom());
        String hourFrom = stimef.format(statistic.getHourFrom());
        String hourTo = stimef.format(statistic.getHourTo());

        Map<String ,Integer> map;
        Map<String ,String> map2 ;
        double p_percent = new Double(0);long z;

        for(i=0;i<=nbJours;i++){

            System.out.println("From : "+dayFrom+" "+hourFrom);

            d1= new Date(dayFrom+" "+hourFrom);
            d3= new Date(dayFrom+" "+hourTo);
            y = d1.getTime()+24*3600*1000;
            d2 = new Date(y);
            z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
            map2 = new HashMap<>();
            map = doWork(engin,d1,d3);
            int arret = (int)z/1000 - map.get("production") - map.get("ralenti");

            if(arret < 0){
                map.put("ralenti",map.get("ralenti")+arret);
                arret = 0 ;
            }
            if(map.get("ralenti")<0) map.put("ralenti",0);

            p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

            map2.put("production", dateUtility.convertToDate(map.get("production")));
            map2.put("ralenti", dateUtility.convertToDate(map.get("ralenti")));
            map2.put("arret", dateUtility.convertToDate(arret));
            map2.put("date", dayFrom);
            map2.put("pause",statistic.getNbHourRepos()+" h");
            map2.put("rendement",String.format("%.2f", p_percent) +" %");
            list.add(map2);

            dayFrom = stf.format(d2);

        }
        return list;
    }

    @Override
    public List<Map<String, String>> getEnginStatisticsTwoWeek(int enginId) {

        List<Map<String,String>> list = new ArrayList<>();
        int i;Date d1,d2,d3 ;
        long y;
        Engin engin = enginRepo.findOne(enginId);

        SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000  ;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);
        int nbJours = statistic.calculNbJours();

        String dayFrom =stf.format(statistic.getDayFrom());
        String hourFrom = stimef.format(statistic.getHourFrom());
        String hourTo = stimef.format(statistic.getHourTo());

        Map<String ,Integer> map;
        Map<String ,String> map2 ;
        double p_percent = new Double(0);long z;

        for(i=0;i<=nbJours;i++){

            System.out.println("From : "+dayFrom+" "+hourFrom);

            d1= new Date(dayFrom+" "+hourFrom);
            d3= new Date(dayFrom+" "+hourTo);
            y = d1.getTime()+24*3600*1000;
            d2 = new Date(y);
            z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
            map2 = new HashMap<>();
            map = doWork(engin,d1,d3);
            int arret = (int)z/1000 - map.get("production") - map.get("ralenti");

            if(arret < 0){
                map.put("ralenti",map.get("ralenti")+arret);
                arret = 0 ;
            }
            if(map.get("ralenti")<0) map.put("ralenti",0);

            p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

            map2.put("production", dateUtility.convertToDate(map.get("production")));
            map2.put("ralenti", dateUtility.convertToDate(map.get("ralenti")));
            map2.put("arret", dateUtility.convertToDate(arret));
            map2.put("date", dayFrom);
            map2.put("pause",statistic.getNbHourRepos()+" h");
            map2.put("rendement",String.format("%.2f", p_percent) +" %");
            list.add(map2);

            dayFrom = stf.format(d2);

        }
        return list;
    }

    @Override
    public List<Map<String, String>> getEnginStatisticsMonth(int enginId) {

        List<Map<String,String>> list = new ArrayList<>();
        int i;Date d1,d2,d3 ;
        long y;
        Engin engin = enginRepo.findOne(enginId);

        SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

        Statistic statistic = new Statistic();
        Date d_to = new Date();
        long week_Ms = d_to.getTime() - 1814400*1000 - 604800*1000 - 302400 * 1000;
        Date d_from = new Date(week_Ms);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,6);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);

        Date time_from = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        Date time_to = cal.getTime();

        statistic.setDayTo(d_to);
        statistic.setDayFrom(d_from);
        statistic.setHourFrom(time_from);
        statistic.setHourTo(time_to);
        statistic.setNbHourRepos(1);
        int nbJours = statistic.calculNbJours();

        String dayFrom =stf.format(statistic.getDayFrom());
        String hourFrom = stimef.format(statistic.getHourFrom());
        String hourTo = stimef.format(statistic.getHourTo());

        Map<String ,Integer> map;
        Map<String ,String> map2 ;
        double p_percent = new Double(0);long z;

        for(i=0;i<=nbJours;i++){

            System.out.println("From : "+dayFrom+" "+hourFrom);

            d1= new Date(dayFrom+" "+hourFrom);
            d3= new Date(dayFrom+" "+hourTo);
            y = d1.getTime()+24*3600*1000;
            d2 = new Date(y);
            z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
            map2 = new HashMap<>();
            map = doWork(engin,d1,d3);
            int arret = (int)z/1000 - map.get("production") - map.get("ralenti");

            if(arret < 0){
                map.put("ralenti",map.get("ralenti")+arret);
                arret = 0 ;
            }
            if(map.get("ralenti")<0) map.put("ralenti",0);

            p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

            map2.put("production", dateUtility.convertToDate(map.get("production")));
            map2.put("ralenti", dateUtility.convertToDate(map.get("ralenti")));
            map2.put("arret", dateUtility.convertToDate(arret));
            map2.put("date", dayFrom);
            map2.put("pause",statistic.getNbHourRepos()+" h");
            map2.put("rendement",String.format("%.2f", p_percent) +" %");
            list.add(map2);

            dayFrom = stf.format(d2);

        }
        return list;
    }

    @Override
	public List<Map<String, String>> getEnginStatistic(int enginId, Statistic statistic) {
		List<Map<String,String>> list = new ArrayList<>();
		int nbJours = statistic.calculNbJours();
		System.out.println(nbJours);
		int i;Date d1,d2,d3 ;
		long y;
		Engin engin = enginRepo.findOne(enginId);


		SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

		String dayFrom =stf.format(statistic.getDayFrom());
		String hourFrom = stimef.format(statistic.getHourFrom());
		String hourTo = stimef.format(statistic.getHourTo());

		Map<String ,Integer> map;
		Map<String ,String> map2 ;
		double p_percent;long z;float fullTime = 0.2f;

		for(i=0;i<=nbJours;i++){

			System.out.println("From : "+dayFrom+" "+hourFrom);

			d1= new Date(dayFrom+" "+hourFrom);
			d3= new Date(dayFrom+" "+hourTo);
			y = d1.getTime()+24*3600*1000;
			d2 = new Date(y);
			z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
			map2 = new HashMap<>();
			map = doWork(engin,d1,d3);
			int arret = (int)z/1000 - map.get("production") - map.get("ralenti");

			if(arret < 0){
				map.put("ralenti",map.get("ralenti")+arret);
				arret = 0 ;
			}
			if(map.get("ralenti")<0) map.put("ralenti",0);

			p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;


			map2.put("production", dateUtility.convertToDate(map.get("production")));
			map2.put("ralenti", dateUtility.convertToDate(map.get("ralenti")));
			map2.put("arret", dateUtility.convertToDate(arret));
			map2.put("date", dayFrom);
			map2.put("pause",statistic.getNbHourRepos()+" h");
			map2.put("rendement",String.format("%.2f", p_percent) +" %");
			list.add(map2);

			dayFrom = stf.format(d2);

		}
		return list;
	}

	@Override
	public List<Map<String, String>> getEnginStatisticsDay(int enginId,String day) {
		List<Map<String,String>> list = new ArrayList<>();
		Map<String ,Integer> map;
		Map<String ,String> map2 ;
		int i;Date d1,d3 ;
		long y,z;
		double p_percent ;
		Engin engin = enginRepo.findOne(enginId);
		String hourTo,hourFrom,dayFrom;
		Date time_from;
		SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,00);
		cal.set(Calendar.MINUTE,00);
		cal.set(Calendar.SECOND,0);

		time_from = cal.getTime();

		Statistic statistic = new Statistic();
		statistic.setDayFrom(new Date(day));
		statistic.setHourFrom(time_from);

		dayFrom =stf.format(statistic.getDayFrom());
		hourFrom = stimef.format(statistic.getHourFrom());

		for(i=0;i<=23;i++){

			d1= new Date(dayFrom+" "+hourFrom);
			y = d1.getTime()+3600*1000;
			hourTo = stimef.format(new Date(y));
            System.out.println(hourFrom);
            System.out.println(hourTo);

            d3= new Date(dayFrom+" "+hourTo);
			z = d3.getTime()-d1.getTime();
			map2 = new HashMap<>();
			map = doWork(engin,d1,d3);
			int arret = (int)z/1000 - map.get("production") - map.get("ralenti");

			if(arret < 0){
				map.put("ralenti",map.get("ralenti")+arret);
				arret = 0 ;
			}
			if(map.get("ralenti")<0) map.put("ralenti",0);

			p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

			map2.put("production", dateUtility.convertToDate(map.get("production")));
			map2.put("ralenti", dateUtility.convertToDate(map.get("ralenti")));
			map2.put("arret", dateUtility.convertToDate(arret));
			map2.put("date", hourFrom+ " -> " +hourTo);
			map2.put("rendement",String.format("%.2f", p_percent) +" %");
			list.add(map2);
            System.out.println(list.size());
            hourFrom = stimef.format(new Date(y));


		}
		return list;
	}

	@Override
	public List<Map<String, String>> getChantierStatisticsDay(int chantierId,String day) {
		List<Map<String,String>> list = new ArrayList<>();
		Map<String ,Integer> map;
		Map<String ,String> map2 ;
		int i,a=0,r=0,p=0;
		Date d1,d3  ;
		long y,z;
		float perc =0.0f;
		double p_percent ;
		String hourTo,hourFrom,dayFrom;
		Date time_from;

		SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,00);
		cal.set(Calendar.MINUTE,00);
		cal.set(Calendar.SECOND,0);

		time_from = cal.getTime();

		Statistic statistic = new Statistic();
		statistic.setDayFrom(new Date(day));
		statistic.setHourFrom(time_from);

		dayFrom =stf.format(statistic.getDayFrom());
		hourFrom = stimef.format(statistic.getHourFrom());

		for(i=0;i<=23;i++){
			d1= new Date(dayFrom+" "+hourFrom);
			y = d1.getTime()+3600*1000;
			hourTo = stimef.format(new Date(y));

			System.out.println(dayFrom + " " + hourFrom);
			System.out.println(dayFrom + " " + hourTo);

			d3= new Date(dayFrom+" "+hourTo);
			z = d3.getTime()-d1.getTime();
			map2 = new HashMap<>();
			List<Engin> listEngin = getEnginList(chantierId);
			for (Engin engin :listEngin) {
				map = doWork(engin,d1,d3);
				int arret = (int)z/1000 - map.get("production") - map.get("ralenti");
				if(arret < 0){
					map.put("ralenti",map.get("ralenti")+arret);
					arret = 0 ;
				}
				if(map.get("ralenti")<0) map.put("ralenti",0);

				p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;

				a+=arret;
				r+=map.get("ralenti");
				p+=map.get("production");
				perc+=p_percent;

			}

			map2.put("production",dateUtility.convertToDate(p/listEngin.size()));
			map2.put("ralenti",dateUtility.convertToDate(r/listEngin.size()));
			map2.put("arret", dateUtility.convertToDate(a/listEngin.size()));
			map2.put("date", hourFrom);
			map2.put("rendement",String.format("%.2f",perc/listEngin.size() )+" %" );
			list.add(map2);
			a=0;r=0;p=0;perc=0.0f;
			map2= new HashMap<>();

			hourFrom = stimef.format(new Date(y));


		}

		System.out.println(list.size());
		return list;
	}

	private List<Engin> getEnginList(int chantierId) {
		return chantierRepo.findOne(chantierId).getEngins();
	}
	private float getChantierRendement(int chantierId, Statistic statistic){
		float rendement = 0.0f;
		List<Engin> listEngins = getEnginList(chantierId);
		for (Engin e:listEngins) {
			rendement = getEnginRendement(e.getId(),statistic);
			System.out.println(rendement+"*********** ok");
		}

		float x = rendement/listEngins.size();
		return x;


	}
	private float getEnginRendement(int enginId,Statistic statistic){

		List<Map<String,String>> list = new ArrayList<>();
		int nbJours = statistic.calculNbJours();
		System.out.println(nbJours);
		int i;Date d1,d2,d3 ;
		long y;
		Engin engin = enginRepo.findOne(enginId);


		SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

		String dayFrom =stf.format(statistic.getDayFrom());
		String hourFrom = stimef.format(statistic.getHourFrom());
		String hourTo = stimef.format(statistic.getHourTo());

		Map<String ,Integer> map;
		Map<String ,String> map2 ;
		float p_percent = 0.0f;long z;float fullTime = 0.2f;
		float x = 0.0f;
		for(i=0;i<=nbJours;i++) {

			System.out.println("From : " + dayFrom + " " + hourFrom);

			d1 = new Date(dayFrom + " " + hourFrom);
			d3 = new Date(dayFrom + " " + hourTo);
			y = d1.getTime() + 24 * 3600 * 1000;
			d2 = new Date(y);
			z = d3.getTime() - d1.getTime() - statistic.getNbHourRepos() * 3600 * 1000;
			map2 = new HashMap<>();
			map = doWork(engin, d1, d3);
			int arret = (int) z / 1000 - map.get("production") - map.get("ralenti");

			if (arret < 0) {
				map.put("ralenti", map.get("ralenti") + arret);
				arret = 0;
			}
			if (map.get("ralenti") < 0) map.put("ralenti", 0);

			p_percent += (float) (map.get("production") * 100 / (z / 1000));

			dayFrom = stf.format(d2);
		}

		return  p_percent / (nbJours+1);

		}
	private Map<String, Integer> doWork(Engin engin,Date from, Date to){
		List<Donnee> donneeList = donneeRepo.findBetween(engin.getId(), from, to);
		return traitementService.calcule(donneeList, engin.getTemps(), engin.getSeuilP(), engin.getSeuilR());
	}
	private List<Map<String, Object>> getEnginStatistique(int enginId, Statistic statistic) {
		List<Map<String,Object>> list = new ArrayList<>();
		int nbJours = statistic.calculNbJours();
		System.out.println(nbJours);
		int i;Date d1,d2,d3 ;
		long y;
		Engin engin = enginRepo.findOne(enginId);


		SimpleDateFormat stf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat stimef = new SimpleDateFormat("HH:mm");

		String dayFrom =stf.format(statistic.getDayFrom());
		String hourFrom = stimef.format(statistic.getHourFrom());
		String hourTo = stimef.format(statistic.getHourTo());

		Map<String ,Integer> map;
		Map<String ,Object> map2 ;
		double p_percent;long z;

		for(i=0;i<=nbJours;i++){

			System.out.println("From : "+dayFrom+" "+hourFrom);

			d1= new Date(dayFrom+" "+hourFrom);
			d3= new Date(dayFrom+" "+hourTo);
			y = d1.getTime()+24*3600*1000;
			d2 = new Date(y);
			z = d3.getTime()-d1.getTime()-statistic.getNbHourRepos()*3600*1000;
			map2 = new HashMap<>();
			map = doWork(engin,d1,d3);
			int arret = (int)z/1000 - map.get("production") - map.get("ralenti");

			if(arret < 0){
				map.put("ralenti",map.get("ralenti")+arret);
				arret = 0 ;
			}
			if(map.get("ralenti")<0) map.put("ralenti",0);

			p_percent =(double) (map.get("production") * 100 / (z/1000) ) ;


			map2.put("production",map.get("production"));
			map2.put("ralenti",map.get("ralenti"));
			map2.put("arret", arret);
			map2.put("date", dayFrom);
			map2.put("pause",statistic.getNbHourRepos());
			map2.put("rendement",p_percent);
			list.add(map2);

			dayFrom = stf.format(d2);

		}
		return list;
	}

}
