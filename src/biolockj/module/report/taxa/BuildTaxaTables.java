/**
 * @UNCC Fodor Lab
 * @author Michael Sioda
 * @email msioda@uncc.edu
 * @date Dec 28, 2018
 * @disclaimer This code is free software; you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any
 * later version, provided that any use properly credits the author. This program is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details at http://www.gnu.org *
 */
package biolockj.module.report.taxa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import biolockj.Log;
import biolockj.module.JavaModule;
import biolockj.module.report.otu.OtuCountModule;
import biolockj.util.*;

/**
 * Many R BioModules expect separate tables containing log-normalized taxa counts for each taxonomy level. This module
 * reads the most recent OTU count files generated by any previous BioModule.
 * 
 * @blj.web_desc Build Taxa Tables
 */
public class BuildTaxaTables extends OtuCountModule implements JavaModule
{

	@Override
	public String getSummary() throws Exception
	{
		return super.getSummary() + summary;
	}

	@Override
	public void runModule() throws Exception
	{
		buildTaxonomyTables( OtuUtil.getSampleOtuCounts( getInputFiles() ) );
	}

	/**
	 * Build taxonomy tables from the sampleTaxaCounts.
	 *
	 * @param sampleOtuCounts TreeMap(SampleId, TreeMap(OTU, count)) OTU counts for every sample
	 * @throws Exception if errors occur
	 */
	protected void buildTaxonomyTables( final TreeMap<String, TreeMap<String, Integer>> sampleOtuCounts )
			throws Exception
	{
		final String label = "OTUs";
		final int pad = SummaryUtil.getPad( label );

		final TreeSet<String> otus = OtuUtil.findUniqueOtus( sampleOtuCounts );
		Log.info( getClass(), "Write " + otus.size() + " unique OTUs for: " + sampleOtuCounts.size() + " samples" );
		report( "OTU Count", sampleOtuCounts );
		report( "Unique OTU", otus );
		summary += BioLockJUtil.addTrailingSpaces( "# Samples:", pad )
				+ BioLockJUtil.formatNumericOutput( sampleOtuCounts.size(), false ) + RETURN;
		int totalOtus = 0;
		boolean topLevel = true;
		for( final String level: TaxaUtil.getTaxaLevels() )
		{
			final TreeSet<String> levelTaxa = TaxaUtil.findUniqueTaxa( otus, level );
			final TreeMap<String, TreeMap<String, Integer>> levelTaxaCounts = TaxaUtil
					.getLevelTaxaCounts( sampleOtuCounts, level );
			final Map<String, Integer> uniqueOtus = new HashMap<>();

			uniqueOtus.put( level, levelTaxa.size() );
			report( "Taxonomy Counts @" + level, levelTaxaCounts );
			final File table = TaxaUtil.getTaxonomyTableFile( getOutputDir(), level, null );
			Log.info( getClass(), "Building: " + table.getAbsolutePath() );

			final BufferedWriter writer = new BufferedWriter( new FileWriter( table ) );
			try
			{
				writer.write( MetaUtil.getID() );
				for( final String taxa: levelTaxa )
				{
					writer.write( TAB_DELIM + taxa );
				}
				writer.write( RETURN );

				for( final String sampleId: sampleOtuCounts.keySet() )
				{
					final TreeMap<String, Integer> taxaCounts = levelTaxaCounts.get( sampleId );
					if( taxaCounts.isEmpty() )
					{
						Log.warn( getClass(), "No " + level + " taxa found: " + sampleId );
						continue;
					}
					writer.write( sampleId );

					for( final String taxa: levelTaxa )
					{
						Integer count = 0;
						if( taxaCounts != null && taxaCounts.keySet().contains( taxa ) )
						{
							count = taxaCounts.get( taxa );
							if( topLevel )
							{
								totalOtus += count;
							}
						}

						writer.write( TAB_DELIM + count );
						Log.debug( getClass(), sampleId + ":" + taxa + "=" + count );
					}

					writer.write( RETURN );
				}

				summary += BioLockJUtil.addTrailingSpaces( "# Unique " + level + " OTUs:", pad )
						+ BioLockJUtil.formatNumericOutput( uniqueOtus.get( level ), false ) + RETURN;
			}
			finally
			{
				if( writer != null )
				{
					writer.close();
				}
			}
			topLevel = false;
		}

		summary += BioLockJUtil.addTrailingSpaces( "# Total OTUs:", pad )
				+ BioLockJUtil.formatNumericOutput( totalOtus, false );
	}

	private void report( final String label, final Collection<String> col ) throws Exception
	{
		if( Log.doDebug() )
		{
			for( final String item: col )
			{
				Log.debug( getClass(), "REPORT [ " + label + " ]:" + item );
			}
		}
	}

	private void report( final String label, final TreeMap<String, TreeMap<String, Integer>> map ) throws Exception
	{
		if( Log.doDebug() )
		{
			for( final String id: map.keySet() )
			{
				final TreeMap<String, Integer> innerMap = map.get( id );
				for( final String otu: innerMap.keySet() )
				{
					Log.debug( getClass(), "REPORT [ " + id + " " + label + " ]: " + otu + "=" + innerMap.get( otu ) );
				}
			}
		}
	}

	private String summary = "";
}