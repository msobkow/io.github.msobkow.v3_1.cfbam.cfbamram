
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
 *	CFBamRamServerListFuncTable in-memory RAM DbIO implementation
 *	for ServerListFunc.
 */
public class CFBamRamServerListFuncTable
	implements ICFBamServerListFuncTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffServerListFunc > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffServerListFunc >();
	private Map< CFBamBuffServerListFuncByRetTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerListFunc >> dictByRetTblIdx
		= new HashMap< CFBamBuffServerListFuncByRetTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerListFunc >>();

	public CFBamRamServerListFuncTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamServerListFunc createServerListFunc( ICFSecAuthorization Authorization,
		ICFBamServerListFunc iBuff )
	{
		final String S_ProcName = "createServerListFunc";
		
		CFBamBuffServerListFunc Buff = (CFBamBuffServerListFunc)(schema.getTableServerMethod().createServerMethod( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffServerListFuncByRetTblIdxKey keyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdictRetTblIdx;
		if( dictByRetTblIdx.containsKey( keyRetTblIdx ) ) {
			subdictRetTblIdx = dictByRetTblIdx.get( keyRetTblIdx );
		}
		else {
			subdictRetTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffServerListFunc >();
			dictByRetTblIdx.put( keyRetTblIdx, subdictRetTblIdx );
		}
		subdictRetTblIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamServerListFunc.CLASS_CODE) {
				CFBamBuffServerListFunc retbuff = ((CFBamBuffServerListFunc)(schema.getFactoryServerListFunc().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamServerListFunc readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerived";
		ICFBamServerListFunc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerListFunc lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerived";
		ICFBamServerListFunc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerListFunc[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerListFunc.readAllDerived";
		ICFBamServerListFunc[] retList = new ICFBamServerListFunc[ dictByPKey.values().size() ];
		Iterator< CFBamBuffServerListFunc > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamServerListFunc[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	public ICFBamServerListFunc readDerivedByUNameIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamServerListFunc ) {
			return( (ICFBamServerListFunc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerListFunc[] readDerivedByMethTableIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	public ICFBamServerListFunc[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	public ICFBamServerListFunc[] readDerivedByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerivedByRetTblIdx";
		CFBamBuffServerListFuncByRetTblIdxKey key = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		key.setOptionalRetTableId( RetTableId );

		ICFBamServerListFunc[] recArray;
		if( dictByRetTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdictRetTblIdx
				= dictByRetTblIdx.get( key );
			recArray = new ICFBamServerListFunc[ subdictRetTblIdx.size() ];
			Iterator< CFBamBuffServerListFunc > iter = subdictRetTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdictRetTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffServerListFunc >();
			dictByRetTblIdx.put( key, subdictRetTblIdx );
			recArray = new ICFBamServerListFunc[0];
		}
		return( recArray );
	}

	public ICFBamServerListFunc readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamServerListFunc buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerListFunc readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readBuff";
		ICFBamServerListFunc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerListFunc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerListFunc lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamServerListFunc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerListFunc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerListFunc[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readAllBuff";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerListFunc.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	public ICFBamServerListFunc readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamServerListFunc buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamServerListFunc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerListFunc[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	public ICFBamServerListFunc readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		ICFBamServerListFunc buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
			return( (ICFBamServerListFunc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerListFunc[] readBuffByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	public ICFBamServerListFunc[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	public ICFBamServerListFunc[] readBuffByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readBuffByRetTblIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByRetTblIdx( Authorization,
			RetTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerListFunc.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
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
	public ICFBamServerListFunc[] pageBuffByMethTableIdx( ICFSecAuthorization Authorization,
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
	public ICFBamServerListFunc[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
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
	public ICFBamServerListFunc[] pageBuffByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRetTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamServerListFunc updateServerListFunc( ICFSecAuthorization Authorization,
		ICFBamServerListFunc iBuff )
	{
		CFBamBuffServerListFunc Buff = (CFBamBuffServerListFunc)schema.getTableServerMethod().updateServerMethod( Authorization,	Buff );
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffServerListFunc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerListFunc",
				"Existing record not found",
				"Existing record not found",
				"ServerListFunc",
				"ServerListFunc",
				pkey );
		}
		CFBamBuffServerListFuncByRetTblIdxKey existingKeyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		existingKeyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		CFBamBuffServerListFuncByRetTblIdxKey newKeyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffServerListFunc >();
			dictByRetTblIdx.put( newKeyRetTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteServerListFunc( ICFSecAuthorization Authorization,
		ICFBamServerListFunc iBuff )
	{
		final String S_ProcName = "CFBamRamServerListFuncTable.deleteServerListFunc() ";
		CFBamBuffServerListFunc Buff = ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffServerListFunc existing = dictByPKey.get( pkey );
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
		CFBamBuffServerListFuncByRetTblIdxKey keyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRetTblIdx.get( keyRetTblIdx );
		subdict.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerListFuncByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRetTableId )
	{
		CFBamBuffServerListFuncByRetTblIdxKey key = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		key.setOptionalRetTableId( argRetTableId );
		deleteServerListFuncByRetTblIdx( Authorization, key );
	}

	public void deleteServerListFuncByRetTblIdx( ICFSecAuthorization Authorization,
		ICFBamServerListFuncByRetTblIdxKey argKey )
	{
		ICFBamServerListFunc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRetTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerListFunc> matchSet = new LinkedList<ICFBamServerListFunc>();
		Iterator<ICFBamServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffServerMethodByUNameIdxKey key = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerListFuncByUNameIdx( Authorization, key );
	}

	public void deleteServerListFuncByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByUNameIdxKey argKey )
	{
		ICFBamServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerListFunc> matchSet = new LinkedList<ICFBamServerListFunc>();
		Iterator<ICFBamServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffServerMethodByMethTableIdxKey key = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerListFuncByMethTableIdx( Authorization, key );
	}

	public void deleteServerListFuncByMethTableIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableIdxKey argKey )
	{
		ICFBamServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerListFunc> matchSet = new LinkedList<ICFBamServerListFunc>();
		Iterator<ICFBamServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffServerMethodByDefSchemaIdxKey key = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerListFuncByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerListFuncByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByDefSchemaIdxKey argKey )
	{
		ICFBamServerListFunc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerListFunc> matchSet = new LinkedList<ICFBamServerListFunc>();
		Iterator<ICFBamServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamServerListFunc cur;
		LinkedList<ICFBamServerListFunc> matchSet = new LinkedList<ICFBamServerListFunc>();
		Iterator<ICFBamServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}

	public void deleteServerListFuncByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerListFuncByTenantIdx( Authorization, key );
	}

	public void deleteServerListFuncByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamServerListFunc> matchSet = new LinkedList<ICFBamServerListFunc>();
		Iterator<ICFBamServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteServerListFunc( Authorization, cur );
		}
	}
}
