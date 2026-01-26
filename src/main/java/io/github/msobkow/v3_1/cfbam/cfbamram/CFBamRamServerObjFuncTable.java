
// Description: Java 25 in-memory RAM DbIO implementation for ServerObjFunc.

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
 *	CFBamRamServerObjFuncTable in-memory RAM DbIO implementation
 *	for ServerObjFunc.
 */
public class CFBamRamServerObjFuncTable
	implements ICFBamServerObjFuncTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffServerObjFunc > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffServerObjFunc >();
	private Map< CFBamBuffServerObjFuncByRetTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerObjFunc >> dictByRetTblIdx
		= new HashMap< CFBamBuffServerObjFuncByRetTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerObjFunc >>();

	public CFBamRamServerObjFuncTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamServerObjFunc createServerObjFunc( ICFSecAuthorization Authorization,
		ICFBamServerObjFunc Buff )
	{
		final String S_ProcName = "createServerObjFunc";
		schema.getTableServerMethod().createServerMethod( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffServerObjFuncByRetTblIdxKey keyRetTblIdx = (CFBamBuffServerObjFuncByRetTblIdxKey)schema.getFactoryServerObjFunc().newByRetTblIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffServerObjFunc > subdictRetTblIdx;
		if( dictByRetTblIdx.containsKey( keyRetTblIdx ) ) {
			subdictRetTblIdx = dictByRetTblIdx.get( keyRetTblIdx );
		}
		else {
			subdictRetTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffServerObjFunc >();
			dictByRetTblIdx.put( keyRetTblIdx, subdictRetTblIdx );
		}
		subdictRetTblIdx.put( pkey, Buff );

		return( Buff );
	}

	public ICFBamServerObjFunc readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readDerived";
		ICFBamServerObjFunc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerObjFunc lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readDerived";
		ICFBamServerObjFunc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerObjFunc[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerObjFunc.readAllDerived";
		ICFBamServerObjFunc[] retList = new ICFBamServerObjFunc[ dictByPKey.values().size() ];
		Iterator< ICFBamServerObjFunc > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamServerObjFunc[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerObjFunc ) ) {
					filteredList.add( (ICFBamServerObjFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
		}
	}

	public ICFBamServerObjFunc readDerivedByUNameIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamServerObjFunc ) {
			return( (ICFBamServerObjFunc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerObjFunc[] readDerivedByMethTableIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerObjFunc ) ) {
					filteredList.add( (ICFBamServerObjFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
		}
	}

	public ICFBamServerObjFunc[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerObjFunc ) ) {
					filteredList.add( (ICFBamServerObjFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
		}
	}

	public ICFBamServerObjFunc[] readDerivedByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readDerivedByRetTblIdx";
		CFBamBuffServerObjFuncByRetTblIdxKey key = (CFBamBuffServerObjFuncByRetTblIdxKey)schema.getFactoryServerObjFunc().newByRetTblIdxKey();
		key.setOptionalRetTableId( RetTableId );

		ICFBamServerObjFunc[] recArray;
		if( dictByRetTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffServerObjFunc > subdictRetTblIdx
				= dictByRetTblIdx.get( key );
			recArray = new ICFBamServerObjFunc[ subdictRetTblIdx.size() ];
			Iterator< ICFBamServerObjFunc > iter = subdictRetTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffServerObjFunc > subdictRetTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffServerObjFunc >();
			dictByRetTblIdx.put( key, subdictRetTblIdx );
			recArray = new ICFBamServerObjFunc[0];
		}
		return( recArray );
	}

	public ICFBamServerObjFunc readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamServerObjFunc buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerObjFunc readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readBuff";
		ICFBamServerObjFunc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerObjFunc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerObjFunc lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamServerObjFunc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerObjFunc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerObjFunc[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readAllBuff";
		ICFBamServerObjFunc buff;
		ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
		ICFBamServerObjFunc[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerObjFunc.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
	}

	public ICFBamServerObjFunc readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamServerObjFunc buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamServerObjFunc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerObjFunc[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamServerObjFunc buff;
		ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
		ICFBamServerObjFunc[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerObjFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
	}

	public ICFBamServerObjFunc readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		ICFBamServerObjFunc buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
			return( (ICFBamServerObjFunc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerObjFunc[] readBuffByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		ICFBamServerObjFunc buff;
		ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
		ICFBamServerObjFunc[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerObjFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
	}

	public ICFBamServerObjFunc[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		ICFBamServerObjFunc buff;
		ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
		ICFBamServerObjFunc[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerObjFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
	}

	public ICFBamServerObjFunc[] readBuffByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerObjFunc.readBuffByRetTblIdx() ";
		ICFBamServerObjFunc buff;
		ArrayList<ICFBamServerObjFunc> filteredList = new ArrayList<ICFBamServerObjFunc>();
		ICFBamServerObjFunc[] buffList = readDerivedByRetTblIdx( Authorization,
			RetTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerObjFunc.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerObjFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerObjFunc[0] ) );
	}

	/**
	 *	Read a page array of the specific ServerObjFunc buffer instances identified by the duplicate key MethTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerObjFunc[] pageBuffByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByMethTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerObjFunc buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerObjFunc[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerObjFunc buffer instances identified by the duplicate key RetTblIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RetTableId	The ServerObjFunc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerObjFunc[] pageBuffByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRetTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamServerObjFunc updateServerObjFunc( ICFSecAuthorization Authorization,
		ICFBamServerObjFunc Buff )
	{
		ICFBamServerObjFunc repl = schema.getTableServerMethod().updateServerMethod( Authorization,
			Buff );
		if (repl != Buff) {
			throw new CFLibInvalidStateException(getClass(), S_ProcName, "repl != Buff", "repl != Buff");
		}
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamServerObjFunc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerObjFunc",
				"Existing record not found",
				"ServerObjFunc",
				pkey );
		}
		CFBamBuffServerObjFuncByRetTblIdxKey existingKeyRetTblIdx = (CFBamBuffServerObjFuncByRetTblIdxKey)schema.getFactoryServerObjFunc().newByRetTblIdxKey();
		existingKeyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		CFBamBuffServerObjFuncByRetTblIdxKey newKeyRetTblIdx = (CFBamBuffServerObjFuncByRetTblIdxKey)schema.getFactoryServerObjFunc().newByRetTblIdxKey();
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
						"updateServerObjFunc",
						"Superclass",
						"SuperClass",
						"ServerMethod",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffServerObjFunc > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffServerObjFunc >();
			dictByRetTblIdx.put( newKeyRetTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteServerObjFunc( ICFSecAuthorization Authorization,
		ICFBamServerObjFunc Buff )
	{
		final String S_ProcName = "CFBamRamServerObjFuncTable.deleteServerObjFunc() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamServerObjFunc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerObjFunc",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckParams[] = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckParams.length > 0 ) {
			schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffServerObjFuncByRetTblIdxKey keyRetTblIdx = (CFBamBuffServerObjFuncByRetTblIdxKey)schema.getFactoryServerObjFunc().newByRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffServerObjFunc > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRetTblIdx.get( keyRetTblIdx );
		subdict.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerObjFuncByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRetTableId )
	{
		CFBamBuffServerObjFuncByRetTblIdxKey key = (CFBamBuffServerObjFuncByRetTblIdxKey)schema.getFactoryServerObjFunc().newByRetTblIdxKey();
		key.setOptionalRetTableId( argRetTableId );
		deleteServerObjFuncByRetTblIdx( Authorization, key );
	}

	public void deleteServerObjFuncByRetTblIdx( ICFSecAuthorization Authorization,
		ICFBamServerObjFuncByRetTblIdxKey argKey )
	{
		ICFBamServerObjFunc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRetTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerObjFunc> matchSet = new LinkedList<ICFBamServerObjFunc>();
		Iterator<ICFBamServerObjFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerObjFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffServerMethodByUNameIdxKey key = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerObjFuncByUNameIdx( Authorization, key );
	}

	public void deleteServerObjFuncByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByUNameIdxKey argKey )
	{
		ICFBamServerObjFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerObjFunc> matchSet = new LinkedList<ICFBamServerObjFunc>();
		Iterator<ICFBamServerObjFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerObjFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffServerMethodByMethTableIdxKey key = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerObjFuncByMethTableIdx( Authorization, key );
	}

	public void deleteServerObjFuncByMethTableIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableIdxKey argKey )
	{
		ICFBamServerObjFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerObjFunc> matchSet = new LinkedList<ICFBamServerObjFunc>();
		Iterator<ICFBamServerObjFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerObjFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffServerMethodByDefSchemaIdxKey key = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerObjFuncByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerObjFuncByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByDefSchemaIdxKey argKey )
	{
		ICFBamServerObjFunc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerObjFunc> matchSet = new LinkedList<ICFBamServerObjFunc>();
		Iterator<ICFBamServerObjFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerObjFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamServerObjFunc cur;
		LinkedList<ICFBamServerObjFunc> matchSet = new LinkedList<ICFBamServerObjFunc>();
		Iterator<ICFBamServerObjFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerObjFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}

	public void deleteServerObjFuncByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerObjFuncByTenantIdx( Authorization, key );
	}

	public void deleteServerObjFuncByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamServerObjFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerObjFunc> matchSet = new LinkedList<ICFBamServerObjFunc>();
		Iterator<ICFBamServerObjFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerObjFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerObjFunc( Authorization, cur );
		}
	}
}
