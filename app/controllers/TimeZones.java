package controllers;

import play.*;
import play.mvc.*;
import play.modules.excel.RenderExcel;
import java.util.*;

public class TimeZones extends Controller {

	public static void index() {
		render();
	}

	public static void list() {
		renderArgs.put(RenderExcel.RA_FILENAME, "time zones.xls");
		final List<TimeZone> timeZones = timeZones();
		render(timeZones);
	}

	private static List<TimeZone> timeZones() {
		final List<TimeZone> timeZones = new ArrayList<TimeZone>();
		for(String id : TimeZone.getAvailableIDs()) {
			if (id.contains("/") && !id.startsWith("Etc/")) {
				timeZones.add(TimeZone.getTimeZone(id));
			}
		}
		return timeZones;
	}

	public static void current() {
		renderArgs.put(RenderExcel.RA_FILENAME, "current time zone.xls");
		final Date date = new Date();
		final TimeZone timeZone = TimeZone.getDefault();
		final List<TimeZone> timeZones = equivalentTimeZones(timeZone);
		render(date, timeZone, timeZones);
	}

	private static List<TimeZone> equivalentTimeZones(final TimeZone timeZone) {
		final List<TimeZone> timeZones = new ArrayList<TimeZone>();
		final long date = new Date().getTime();
		for(String id : TimeZone.getAvailableIDs()) {
			final TimeZone tz = TimeZone.getTimeZone(id);
			if (id.contains("/") && !id.startsWith("Etc/") && tz.getOffset(date) == timeZone.getOffset(date)) {
				timeZones.add(tz);
			}
		}
		return timeZones;
	}

	public static void regions() {
		renderArgs.put(RenderExcel.RA_FILENAME, "time zones by region.xls");
		final SortedSet<String> regions = timeZoneRegions();
		final List<TimeZone> timeZones = timeZones();
		render(regions, timeZones);
	}

	private static SortedSet<String> timeZoneRegions() {
		final SortedSet<String> regions = new TreeSet<String>();
		for(String id : TimeZone.getAvailableIDs()) {
			if (id.contains("/") && !id.startsWith("Etc/")) {
				regions.add(id.split("/")[0]);
			}
		}
		return regions;
	}

}
