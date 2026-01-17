
// Description: Java 25 in-memory RAM DbIO implementation for ServerListFunc.

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
 *	CFBamRamServerListFuncTable in-memory RAM DbIO implementation
 *	for ServerListFunc.
 */
public class CFBamRamServerListFuncTable
	implements ICFBamServerListFuncTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamServerListFuncBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamServerListFuncBuff >();
	private Map< CFBamServerListFuncByRetTblIdxKey,
				Map< CFBamScopePKey,
					CFBamServerListFuncBuff >> dictByRetTblIdx
		= new HashMap< CFBamServerListFuncByRetTblIdxKey,
				Map< CFBamScopePKey,
					CFBamServerListFuncBuff >>();

	public CFBamRamServerListFuncTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createServerListFunc( CFSecAuthorization Authorization,
		CFBamServerListFuncBuff Buff )
	{
		final String S_ProcName = "createServerListFunc";
		schema.getTableServerMethod().createServerMethod( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerListFuncByRetTblIdxKey keyRetTblIdx = schema.getFactoryServerListFunc().newRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTableId( Buff.getOptionalRetTableId() );

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

		Map< CFBamScopePKey, CFBamServerListFuncBuff > subdictRetTblIdx;
		if( dictByRetTblIdx.containsKey( keyRetTblIdx ) ) {
			subdictRetTblIdx = dictByRetTblIdx.get( keyRetTblIdx );
		}
		else {
			subdictRetTblIdx = new HashMap< CFBamScopePKey, CFBamServerListFuncBuff >();
			dictByRetTblIdx.put( keyRetTblIdx, subdictRetTblIdx );
		}
		subdictRetTblIdx.put( pkey, Buff );

	}

	public CFBamServerListFuncBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerListFuncBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerListFuncBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerListFuncBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerListFuncBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerListFunc.readAllDerived";
		CFBamServerListFuncBuff[] retList = new CFBamServerListFuncBuff[ dictByPKey.values().size() ];
		Iterator< CFBamServerListFuncBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamServerListFuncBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerListFuncBuff ) ) {
					filteredList.add( (CFBamServerListFuncBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
		}
	}

	public CFBamServerListFuncBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
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
		else if( buff instanceof CFBamServerListFuncBuff ) {
			return( (CFBamServerListFuncBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerListFuncBuff[] readDerivedByMethTableIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerListFuncBuff ) ) {
					filteredList.add( (CFBamServerListFuncBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
		}
	}

	public CFBamServerListFuncBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerListFuncBuff ) ) {
					filteredList.add( (CFBamServerListFuncBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
		}
	}

	public CFBamServerListFuncBuff[] readDerivedByRetTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerivedByRetTblIdx";
		CFBamServerListFuncByRetTblIdxKey key = schema.getFactoryServerListFunc().newRetTblIdxKey();
		key.setOptionalRetTableId( RetTableId );

		CFBamServerListFuncBuff[] recArray;
		if( dictByRetTblIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamServerListFuncBuff > subdictRetTblIdx
				= dictByRetTblIdx.get( key );
			recArray = new CFBamServerListFuncBuff[ subdictRetTblIdx.size() ];
			Iterator< CFBamServerListFuncBuff > iter = subdictRetTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamServerListFuncBuff > subdictRetTblIdx
				= new HashMap< CFBamScopePKey, CFBamServerListFuncBuff >();
			dictByRetTblIdx.put( key, subdictRetTblIdx );
			recArray = new CFBamServerListFuncBuff[0];
		}
		return( recArray );
	}

	public CFBamServerListFuncBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamServerListFuncBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerListFuncBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readBuff";
		CFBamServerListFuncBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a837" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerListFuncBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamServerListFuncBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a837" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerListFuncBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readAllBuff";
		CFBamServerListFuncBuff buff;
		ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
		CFBamServerListFuncBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a837" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
	}

	public CFBamServerListFuncBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamServerListFuncBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamServerListFuncBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerListFuncBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamServerListFuncBuff buff;
		ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
		CFBamServerListFuncBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamServerListFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
	}

	public CFBamServerListFuncBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		CFBamServerListFuncBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
			return( (CFBamServerListFuncBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerListFuncBuff[] readBuffByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		CFBamServerListFuncBuff buff;
		ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
		CFBamServerListFuncBuff[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerListFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
	}

	public CFBamServerListFuncBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		CFBamServerListFuncBuff buff;
		ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
		CFBamServerListFuncBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerListFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
	}

	public CFBamServerListFuncBuff[] readBuffByRetTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readBuffByRetTblIdx() ";
		CFBamServerListFuncBuff buff;
		ArrayList<CFBamServerListFuncBuff> filteredList = new ArrayList<CFBamServerListFuncBuff>();
		CFBamServerListFuncBuff[] buffList = readDerivedByRetTblIdx( Authorization,
			RetTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a837" ) ) {
				filteredList.add( (CFBamServerListFuncBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerListFuncBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific ServerListFunc buffer instances identified by the duplicate key MethTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The ServerListFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerListFuncBuff[] pageBuffByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByMethTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerListFunc buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ServerListFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerListFuncBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerListFunc buffer instances identified by the duplicate key RetTblIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RetTableId	The ServerListFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerListFuncBuff[] pageBuffByRetTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRetTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateServerListFunc( CFSecAuthorization Authorization,
		CFBamServerListFuncBuff Buff )
	{
		schema.getTableServerMethod().updateServerMethod( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerListFuncBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerListFunc",
				"Existing record not found",
				"ServerListFunc",
				pkey );
		}
		CFBamServerListFuncByRetTblIdxKey existingKeyRetTblIdx = schema.getFactoryServerListFunc().newRetTblIdxKey();
		existingKeyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		CFBamServerListFuncByRetTblIdxKey newKeyRetTblIdx = schema.getFactoryServerListFunc().newRetTblIdxKey();
		newKeyRetTblIdx.setOptionalRetTableId( Buff.getOptionalRetTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerListFunc",
						"Superclass",
						"SuperClass",
						"ServerMethod",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamServerListFuncBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByRetTblIdx.get( existingKeyRetTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRetTblIdx.containsKey( newKeyRetTblIdx ) ) {
			subdict = dictByRetTblIdx.get( newKeyRetTblIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamServerListFuncBuff >();
			dictByRetTblIdx.put( newKeyRetTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteServerListFunc( CFSecAuthorization Authorization,
		CFBamServerListFuncBuff Buff )
	{
		final String S_ProcName = "CFBamRamServerListFuncTable.deleteServerListFunc() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerListFuncBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerListFunc",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckParams[] = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckParams.length > 0 ) {
			schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamServerListFuncByRetTblIdxKey keyRetTblIdx = schema.getFactoryServerListFunc().newRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamServerListFuncBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRetTblIdx.get( keyRetTblIdx );
		subdict.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerListFuncByRetTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRetTableId )
	{
		CFBamServerListFuncByRetTblIdxKey key = schema.getFactoryServerListFunc().newRetTblIdxKey();
		key.setOptionalRetTableId( argRetTableId );
		deleteServerListFuncByRetTblIdx( Authorization, key );
	}

	public void deleteServerListFuncByRetTblIdx( CFSecAuthorization Authorization,
		CFBamServerListFuncByRetTblIdxKey argKey )
	{
		CFBamServerListFuncBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRetTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerListFuncBuff> matchSet = new LinkedList<CFBamServerListFuncBuff>();
		Iterator<CFBamServerListFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerListFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamServerMethodByUNameIdxKey key = schema.getFactoryServerMethod().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerListFuncByUNameIdx( Authorization, key );
	}

	public void deleteServerListFuncByUNameIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByUNameIdxKey argKey )
	{
		CFBamServerListFuncBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerListFuncBuff> matchSet = new LinkedList<CFBamServerListFuncBuff>();
		Iterator<CFBamServerListFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerListFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamServerMethodByMethTableIdxKey key = schema.getFactoryServerMethod().newMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerListFuncByMethTableIdx( Authorization, key );
	}

	public void deleteServerListFuncByMethTableIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByMethTableIdxKey argKey )
	{
		CFBamServerListFuncBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerListFuncBuff> matchSet = new LinkedList<CFBamServerListFuncBuff>();
		Iterator<CFBamServerListFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerListFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamServerMethodByDefSchemaIdxKey key = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerListFuncByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerListFuncByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByDefSchemaIdxKey argKey )
	{
		CFBamServerListFuncBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerListFuncBuff> matchSet = new LinkedList<CFBamServerListFuncBuff>();
		Iterator<CFBamServerListFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerListFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteServerListFuncByIdIdx( Authorization, key );
	}

	public void deleteServerListFuncByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamServerListFuncBuff cur;
		LinkedList<CFBamServerListFuncBuff> matchSet = new LinkedList<CFBamServerListFuncBuff>();
		Iterator<CFBamServerListFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerListFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerListFuncByTenantIdx( Authorization, key );
	}

	public void deleteServerListFuncByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamServerListFuncBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerListFuncBuff> matchSet = new LinkedList<CFBamServerListFuncBuff>();
		Iterator<CFBamServerListFuncBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerListFuncBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}
}
