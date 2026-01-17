
// Description: Java 25 in-memory RAM DbIO implementation for ClearSubDep2.

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
 *	CFBamRamClearSubDep2Table in-memory RAM DbIO implementation
 *	for ClearSubDep2.
 */
public class CFBamRamClearSubDep2Table
	implements ICFBamClearSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamClearSubDep2Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamClearSubDep2Buff >();
	private Map< CFBamClearSubDep2ByClearSubDep1IdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep2Buff >> dictByClearSubDep1Idx
		= new HashMap< CFBamClearSubDep2ByClearSubDep1IdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep2Buff >>();
	private Map< CFBamClearSubDep2ByUNameIdxKey,
			CFBamClearSubDep2Buff > dictByUNameIdx
		= new HashMap< CFBamClearSubDep2ByUNameIdxKey,
			CFBamClearSubDep2Buff >();

	public CFBamRamClearSubDep2Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearSubDep2( CFSecAuthorization Authorization,
		CFBamClearSubDep2Buff Buff )
	{
		final String S_ProcName = "createClearSubDep2";
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep2ByClearSubDep1IdxKey keyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		keyClearSubDep1Idx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );

		CFBamClearSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep2UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"ClearDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ClearSubDep1",
						"ClearSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearSubDep2Buff > subdictClearSubDep1Idx;
		if( dictByClearSubDep1Idx.containsKey( keyClearSubDep1Idx ) ) {
			subdictClearSubDep1Idx = dictByClearSubDep1Idx.get( keyClearSubDep1Idx );
		}
		else {
			subdictClearSubDep1Idx = new HashMap< CFBamScopePKey, CFBamClearSubDep2Buff >();
			dictByClearSubDep1Idx.put( keyClearSubDep1Idx, subdictClearSubDep1Idx );
		}
		subdictClearSubDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamClearSubDep2Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep2Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep2Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep2.readAllDerived";
		CFBamClearSubDep2Buff[] retList = new CFBamClearSubDep2Buff[ dictByPKey.values().size() ];
		Iterator< CFBamClearSubDep2Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamClearSubDep2Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep2Buff ) ) {
					filteredList.add( (CFBamClearSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
		}
	}

	public CFBamClearSubDep2Buff[] readDerivedByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		CFBamClearDepBuff buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamClearDepBuff buff;
			ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep2Buff ) ) {
					filteredList.add( (CFBamClearSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
		}
	}

	public CFBamClearSubDep2Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		CFBamClearDepBuff buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamClearDepBuff buff;
			ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep2Buff ) ) {
					filteredList.add( (CFBamClearSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
		}
	}

	public CFBamClearSubDep2Buff[] readDerivedByClearSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerivedByClearSubDep1Idx";
		CFBamClearSubDep2ByClearSubDep1IdxKey key = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		key.setRequiredClearSubDep1Id( ClearSubDep1Id );

		CFBamClearSubDep2Buff[] recArray;
		if( dictByClearSubDep1Idx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearSubDep2Buff > subdictClearSubDep1Idx
				= dictByClearSubDep1Idx.get( key );
			recArray = new CFBamClearSubDep2Buff[ subdictClearSubDep1Idx.size() ];
			Iterator< CFBamClearSubDep2Buff > iter = subdictClearSubDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearSubDep2Buff > subdictClearSubDep1Idx
				= new HashMap< CFBamScopePKey, CFBamClearSubDep2Buff >();
			dictByClearSubDep1Idx.put( key, subdictClearSubDep1Idx );
			recArray = new CFBamClearSubDep2Buff[0];
		}
		return( recArray );
	}

	public CFBamClearSubDep2Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerivedByUNameIdx";
		CFBamClearSubDep2ByUNameIdxKey key = schema.getFactoryClearSubDep2().newUNameIdxKey();
		key.setRequiredClearSubDep1Id( ClearSubDep1Id );
		key.setRequiredName( Name );

		CFBamClearSubDep2Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep2Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamClearSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep2Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readBuff";
		CFBamClearSubDep2Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a812" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep2Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamClearSubDep2Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a812" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep2Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readAllBuff";
		CFBamClearSubDep2Buff buff;
		ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
		CFBamClearSubDep2Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a812" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
	}

	public CFBamClearSubDep2Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamClearSubDep2Buff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamClearSubDep2Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearSubDep2Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamClearSubDep2Buff buff;
		ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
		CFBamClearSubDep2Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamClearSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
	}

	public CFBamClearSubDep2Buff[] readBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		CFBamClearSubDep2Buff buff;
		ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
		CFBamClearSubDep2Buff[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
	}

	public CFBamClearSubDep2Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		CFBamClearSubDep2Buff buff;
		ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
		CFBamClearSubDep2Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
	}

	public CFBamClearSubDep2Buff[] readBuffByClearSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readBuffByClearSubDep1Idx() ";
		CFBamClearSubDep2Buff buff;
		ArrayList<CFBamClearSubDep2Buff> filteredList = new ArrayList<CFBamClearSubDep2Buff>();
		CFBamClearSubDep2Buff[] buffList = readDerivedByClearSubDep1Idx( Authorization,
			ClearSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a812" ) ) {
				filteredList.add( (CFBamClearSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep2Buff[0] ) );
	}

	public CFBamClearSubDep2Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readBuffByUNameIdx() ";
		CFBamClearSubDep2Buff buff = readDerivedByUNameIdx( Authorization,
			ClearSubDep1Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a812" ) ) {
			return( (CFBamClearSubDep2Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific ClearSubDep2 buffer instances identified by the duplicate key ClearDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The ClearSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep2Buff[] pageBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep2 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ClearSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep2Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep2 buffer instances identified by the duplicate key ClearSubDep1Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ClearSubDep1Id	The ClearSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep2Buff[] pageBuffByClearSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearSubDep1Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateClearSubDep2( CFSecAuthorization Authorization,
		CFBamClearSubDep2Buff Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep2Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep2",
				"Existing record not found",
				"ClearSubDep2",
				pkey );
		}
		CFBamClearSubDep2ByClearSubDep1IdxKey existingKeyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		existingKeyClearSubDep1Idx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );

		CFBamClearSubDep2ByClearSubDep1IdxKey newKeyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		newKeyClearSubDep1Idx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );

		CFBamClearSubDep2ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamClearSubDep2ByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
		newKeyUNameIdx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep2",
					"ClearSubDep2UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep2",
						"Superclass",
						"SuperClass",
						"ClearDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep2",
						"Container",
						"ClearSubDep1",
						"ClearSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamClearSubDep2Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearSubDep1Idx.get( existingKeyClearSubDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearSubDep1Idx.containsKey( newKeyClearSubDep1Idx ) ) {
			subdict = dictByClearSubDep1Idx.get( newKeyClearSubDep1Idx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearSubDep2Buff >();
			dictByClearSubDep1Idx.put( newKeyClearSubDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteClearSubDep2( CFSecAuthorization Authorization,
		CFBamClearSubDep2Buff Buff )
	{
		final String S_ProcName = "CFBamRamClearSubDep2Table.deleteClearSubDep2() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep2Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep3().readDerivedByClearSubDep2Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep3().deleteClearSubDep3ByClearSubDep2Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamClearSubDep2ByClearSubDep1IdxKey keyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		keyClearSubDep1Idx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );

		CFBamClearSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamClearSubDep2Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearSubDep1Idx.get( keyClearSubDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearSubDep2ByClearSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep1Id )
	{
		CFBamClearSubDep2ByClearSubDep1IdxKey key = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		key.setRequiredClearSubDep1Id( argClearSubDep1Id );
		deleteClearSubDep2ByClearSubDep1Idx( Authorization, key );
	}

	public void deleteClearSubDep2ByClearSubDep1Idx( CFSecAuthorization Authorization,
		CFBamClearSubDep2ByClearSubDep1IdxKey argKey )
	{
		CFBamClearSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep2Buff> matchSet = new LinkedList<CFBamClearSubDep2Buff>();
		Iterator<CFBamClearSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep1Id,
		String argName )
	{
		CFBamClearSubDep2ByUNameIdxKey key = schema.getFactoryClearSubDep2().newUNameIdxKey();
		key.setRequiredClearSubDep1Id( argClearSubDep1Id );
		key.setRequiredName( argName );
		deleteClearSubDep2ByUNameIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByUNameIdx( CFSecAuthorization Authorization,
		CFBamClearSubDep2ByUNameIdxKey argKey )
	{
		CFBamClearSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep2Buff> matchSet = new LinkedList<CFBamClearSubDep2Buff>();
		Iterator<CFBamClearSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep2ByClearDepIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByClearDepIdx( CFSecAuthorization Authorization,
		CFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamClearSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep2Buff> matchSet = new LinkedList<CFBamClearSubDep2Buff>();
		Iterator<CFBamClearSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep2ByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamClearSubDep2Buff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep2Buff> matchSet = new LinkedList<CFBamClearSubDep2Buff>();
		Iterator<CFBamClearSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearSubDep2ByIdIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamClearSubDep2Buff cur;
		LinkedList<CFBamClearSubDep2Buff> matchSet = new LinkedList<CFBamClearSubDep2Buff>();
		Iterator<CFBamClearSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep2ByTenantIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamClearSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep2Buff> matchSet = new LinkedList<CFBamClearSubDep2Buff>();
		Iterator<CFBamClearSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}
}
