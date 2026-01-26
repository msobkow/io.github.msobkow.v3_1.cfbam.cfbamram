
// Description: Java 25 in-memory RAM DbIO implementation for ServerProc.

/*
 *	io.github.msobkow.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFBam - Business Application Model
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 *	
 */

package io.github.msobkow.v3_1.cfbam.cfbamram;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfsec.cfsec.buff.*;
import io.github.msobkow.v3_1.cfint.cfint.buff.*;
import io.github.msobkow.v3_1.cfbam.cfbam.buff.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;

/*
 *	CFBamRamServerProcTable in-memory RAM DbIO implementation
 *	for ServerProc.
 */
public class CFBamRamServerProcTable
	implements ICFBamServerProcTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffServerProc > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffServerProc >();

	public CFBamRamServerProcTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createServerProc( ICFSecAuthorization Authorization,
		ICFBamServerProc Buff )
	{
		final String S_ProcName = "createServerProc";
		schema.getTableServerMethod().createServerMethod( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"ServerMethod",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

	}

	public ICFBamServerProc readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readDerived";
		ICFBamServerProc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamServerProc buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerProc.readAllDerived";
		ICFBamServerProc[] retList = new ICFBamServerProc[ dictByPKey.values().size() ];
		Iterator< ICFBamServerProc > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamServerProc[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		ICFBamScope buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamScope buff;
			ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerProc ) ) {
					filteredList.add( (ICFBamServerProc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerProc[0] ) );
		}
	}

	public ICFBamServerProc readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		ICFBamServerMethod buff = schema.getTableServerMethod().readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamServerProc ) {
			return( (ICFBamServerProc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerProc[] readDerivedByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByMethTableIdx( Authorization,
			TableId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerProc ) ) {
					filteredList.add( (ICFBamServerProc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerProc[0] ) );
		}
	}

	public ICFBamServerProc[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerProc ) ) {
					filteredList.add( (ICFBamServerProc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerProc[0] ) );
		}
	}

	public ICFBamServerProc readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		ICFBamServerProc buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readBuff";
		ICFBamServerProc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a807" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamServerProc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a807" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerProc.readAllBuff";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a807" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	public ICFBamServerProc readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamServerProc buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (ICFBamServerProc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerProc[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (ICFBamServerProc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	public ICFBamServerProc readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		ICFBamServerProc buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
			return( (ICFBamServerProc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerProc[] readBuffByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (ICFBamServerProc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	public ICFBamServerProc[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (ICFBamServerProc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	/**
	 *	Read a page array of the specific ServerProc buffer instances identified by the duplicate key MethTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The ServerProc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerProc[] pageBuffByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByMethTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerProc buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ServerProc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerProc[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateServerProc( ICFSecAuthorization Authorization,
		ICFBamServerProc Buff )
	{
		schema.getTableServerMethod().updateServerMethod( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamServerProc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerProc",
				"Existing record not found",
				"ServerProc",
				pkey );
		}
		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerProc",
						"Superclass",
						"SuperClass",
						"ServerMethod",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffServerProc > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

	}

	public void deleteServerProc( ICFSecAuthorization Authorization,
		ICFBamServerProc Buff )
	{
		final String S_ProcName = "CFBamRamServerProcTable.deleteServerProc() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamServerProc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerProc",
				pkey );
		}
					schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffServerProc > subdict;

		dictByPKey.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerProcByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffServerMethodByUNameIdxKey key = schema.getFactoryServerMethod().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerProcByUNameIdx( Authorization, key );
	}

	public void deleteServerProcByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByUNameIdxKey argKey )
	{
		ICFBamServerProc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerProc> matchSet = new LinkedList<ICFBamServerProc>();
		Iterator<ICFBamServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffServerMethodByMethTableIdxKey key = schema.getFactoryServerMethod().newMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerProcByMethTableIdx( Authorization, key );
	}

	public void deleteServerProcByMethTableIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableIdxKey argKey )
	{
		ICFBamServerProc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerProc> matchSet = new LinkedList<ICFBamServerProc>();
		Iterator<ICFBamServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffServerMethodByDefSchemaIdxKey key = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerProcByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerProcByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByDefSchemaIdxKey argKey )
	{
		ICFBamServerProc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerProc> matchSet = new LinkedList<ICFBamServerProc>();
		Iterator<ICFBamServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteServerProcByIdIdx( Authorization, key );
	}

	public void deleteServerProcByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamServerProc cur;
		LinkedList<ICFBamServerProc> matchSet = new LinkedList<ICFBamServerProc>();
		Iterator<ICFBamServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerProcByTenantIdx( Authorization, key );
	}

	public void deleteServerProcByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamServerProc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerProc> matchSet = new LinkedList<ICFBamServerProc>();
		Iterator<ICFBamServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}
}
