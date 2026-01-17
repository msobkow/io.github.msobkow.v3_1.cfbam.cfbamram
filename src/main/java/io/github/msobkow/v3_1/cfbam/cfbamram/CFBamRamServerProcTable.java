
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
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;
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
	private Map< CFBamScopePKey,
				CFBamServerProcBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamServerProcBuff >();

	public CFBamRamServerProcTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createServerProc( CFSecAuthorization Authorization,
		CFBamServerProcBuff Buff )
	{
		final String S_ProcName = "createServerProc";
		schema.getTableServerMethod().createServerMethod( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
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

	public CFBamServerProcBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerProcBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerProcBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerProcBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerProcBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerProc.readAllDerived";
		CFBamServerProcBuff[] retList = new CFBamServerProcBuff[ dictByPKey.values().size() ];
		Iterator< CFBamServerProcBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamServerProcBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamScopeBuff buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamScopeBuff buff;
			ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerProcBuff ) ) {
					filteredList.add( (CFBamServerProcBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
		}
	}

	public CFBamServerProcBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		CFBamServerMethodBuff buff = schema.getTableServerMethod().readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof CFBamServerProcBuff ) {
			return( (CFBamServerProcBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerProcBuff[] readDerivedByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		CFBamServerMethodBuff buffList[] = schema.getTableServerMethod().readDerivedByMethTableIdx( Authorization,
			TableId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamServerMethodBuff buff;
			ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerProcBuff ) ) {
					filteredList.add( (CFBamServerProcBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
		}
	}

	public CFBamServerProcBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		CFBamServerMethodBuff buffList[] = schema.getTableServerMethod().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamServerMethodBuff buff;
			ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerProcBuff ) ) {
					filteredList.add( (CFBamServerProcBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
		}
	}

	public CFBamServerProcBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamServerProcBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerProcBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readBuff";
		CFBamServerProcBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a807" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerProcBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamServerProcBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a807" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerProcBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerProc.readAllBuff";
		CFBamServerProcBuff buff;
		ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
		CFBamServerProcBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a807" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
	}

	public CFBamServerProcBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamServerProcBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamServerProcBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerProcBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamServerProcBuff buff;
		ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
		CFBamServerProcBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamServerProcBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
	}

	public CFBamServerProcBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		CFBamServerProcBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
			return( (CFBamServerProcBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerProcBuff[] readBuffByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		CFBamServerProcBuff buff;
		ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
		CFBamServerProcBuff[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerProcBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
	}

	public CFBamServerProcBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		CFBamServerProcBuff buff;
		ArrayList<CFBamServerProcBuff> filteredList = new ArrayList<CFBamServerProcBuff>();
		CFBamServerProcBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerProcBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerProcBuff[0] ) );
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
	public CFBamServerProcBuff[] pageBuffByMethTableIdx( CFSecAuthorization Authorization,
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
	public CFBamServerProcBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateServerProc( CFSecAuthorization Authorization,
		CFBamServerProcBuff Buff )
	{
		schema.getTableServerMethod().updateServerMethod( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerProcBuff existing = dictByPKey.get( pkey );
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

		Map< CFBamScopePKey, CFBamServerProcBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

	}

	public void deleteServerProc( CFSecAuthorization Authorization,
		CFBamServerProcBuff Buff )
	{
		final String S_ProcName = "CFBamRamServerProcTable.deleteServerProc() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerProcBuff existing = dictByPKey.get( pkey );
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
		Map< CFBamScopePKey, CFBamServerProcBuff > subdict;

		dictByPKey.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerProcByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamServerMethodByUNameIdxKey key = schema.getFactoryServerMethod().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerProcByUNameIdx( Authorization, key );
	}

	public void deleteServerProcByUNameIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByUNameIdxKey argKey )
	{
		CFBamServerProcBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerProcBuff> matchSet = new LinkedList<CFBamServerProcBuff>();
		Iterator<CFBamServerProcBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerProcBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamServerMethodByMethTableIdxKey key = schema.getFactoryServerMethod().newMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerProcByMethTableIdx( Authorization, key );
	}

	public void deleteServerProcByMethTableIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByMethTableIdxKey argKey )
	{
		CFBamServerProcBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerProcBuff> matchSet = new LinkedList<CFBamServerProcBuff>();
		Iterator<CFBamServerProcBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerProcBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamServerMethodByDefSchemaIdxKey key = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerProcByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerProcByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByDefSchemaIdxKey argKey )
	{
		CFBamServerProcBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerProcBuff> matchSet = new LinkedList<CFBamServerProcBuff>();
		Iterator<CFBamServerProcBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerProcBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteServerProcByIdIdx( Authorization, key );
	}

	public void deleteServerProcByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamServerProcBuff cur;
		LinkedList<CFBamServerProcBuff> matchSet = new LinkedList<CFBamServerProcBuff>();
		Iterator<CFBamServerProcBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerProcBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerProcByTenantIdx( Authorization, key );
	}

	public void deleteServerProcByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamServerProcBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerProcBuff> matchSet = new LinkedList<CFBamServerProcBuff>();
		Iterator<CFBamServerProcBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerProcBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerProc( Authorization, cur );
		}
	}
}
