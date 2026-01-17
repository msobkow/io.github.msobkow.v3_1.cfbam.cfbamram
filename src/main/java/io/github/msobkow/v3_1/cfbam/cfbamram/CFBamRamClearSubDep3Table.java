
// Description: Java 25 in-memory RAM DbIO implementation for ClearSubDep3.

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
 *	CFBamRamClearSubDep3Table in-memory RAM DbIO implementation
 *	for ClearSubDep3.
 */
public class CFBamRamClearSubDep3Table
	implements ICFBamClearSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamClearSubDep3Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamClearSubDep3Buff >();
	private Map< CFBamClearSubDep3ByClearSubDep2IdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep3Buff >> dictByClearSubDep2Idx
		= new HashMap< CFBamClearSubDep3ByClearSubDep2IdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep3Buff >>();
	private Map< CFBamClearSubDep3ByUNameIdxKey,
			CFBamClearSubDep3Buff > dictByUNameIdx
		= new HashMap< CFBamClearSubDep3ByUNameIdxKey,
			CFBamClearSubDep3Buff >();

	public CFBamRamClearSubDep3Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearSubDep3( CFSecAuthorization Authorization,
		CFBamClearSubDep3Buff Buff )
	{
		final String S_ProcName = "createClearSubDep3";
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep3ByClearSubDep2IdxKey keyClearSubDep2Idx = schema.getFactoryClearSubDep3().newClearSubDep2IdxKey();
		keyClearSubDep2Idx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );

		CFBamClearSubDep3ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep3().newUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep3UNameIdx",
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
				if( null == schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ClearSubDep2",
						"ClearSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearSubDep3Buff > subdictClearSubDep2Idx;
		if( dictByClearSubDep2Idx.containsKey( keyClearSubDep2Idx ) ) {
			subdictClearSubDep2Idx = dictByClearSubDep2Idx.get( keyClearSubDep2Idx );
		}
		else {
			subdictClearSubDep2Idx = new HashMap< CFBamScopePKey, CFBamClearSubDep3Buff >();
			dictByClearSubDep2Idx.put( keyClearSubDep2Idx, subdictClearSubDep2Idx );
		}
		subdictClearSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamClearSubDep3Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep3Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep3Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep3.readAllDerived";
		CFBamClearSubDep3Buff[] retList = new CFBamClearSubDep3Buff[ dictByPKey.values().size() ];
		Iterator< CFBamClearSubDep3Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamClearSubDep3Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep3Buff ) ) {
					filteredList.add( (CFBamClearSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
		}
	}

	public CFBamClearSubDep3Buff[] readDerivedByClearDepIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep3Buff ) ) {
					filteredList.add( (CFBamClearSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
		}
	}

	public CFBamClearSubDep3Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep3Buff ) ) {
					filteredList.add( (CFBamClearSubDep3Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
		}
	}

	public CFBamClearSubDep3Buff[] readDerivedByClearSubDep2Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerivedByClearSubDep2Idx";
		CFBamClearSubDep3ByClearSubDep2IdxKey key = schema.getFactoryClearSubDep3().newClearSubDep2IdxKey();
		key.setRequiredClearSubDep2Id( ClearSubDep2Id );

		CFBamClearSubDep3Buff[] recArray;
		if( dictByClearSubDep2Idx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearSubDep3Buff > subdictClearSubDep2Idx
				= dictByClearSubDep2Idx.get( key );
			recArray = new CFBamClearSubDep3Buff[ subdictClearSubDep2Idx.size() ];
			Iterator< CFBamClearSubDep3Buff > iter = subdictClearSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearSubDep3Buff > subdictClearSubDep2Idx
				= new HashMap< CFBamScopePKey, CFBamClearSubDep3Buff >();
			dictByClearSubDep2Idx.put( key, subdictClearSubDep2Idx );
			recArray = new CFBamClearSubDep3Buff[0];
		}
		return( recArray );
	}

	public CFBamClearSubDep3Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerivedByUNameIdx";
		CFBamClearSubDep3ByUNameIdxKey key = schema.getFactoryClearSubDep3().newUNameIdxKey();
		key.setRequiredClearSubDep2Id( ClearSubDep2Id );
		key.setRequiredName( Name );

		CFBamClearSubDep3Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep3Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamClearSubDep3Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep3Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readBuff";
		CFBamClearSubDep3Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a813" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep3Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamClearSubDep3Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a813" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep3Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readAllBuff";
		CFBamClearSubDep3Buff buff;
		ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
		CFBamClearSubDep3Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a813" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
	}

	public CFBamClearSubDep3Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamClearSubDep3Buff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamClearSubDep3Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearSubDep3Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamClearSubDep3Buff buff;
		ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
		CFBamClearSubDep3Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamClearSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
	}

	public CFBamClearSubDep3Buff[] readBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		CFBamClearSubDep3Buff buff;
		ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
		CFBamClearSubDep3Buff[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
	}

	public CFBamClearSubDep3Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		CFBamClearSubDep3Buff buff;
		ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
		CFBamClearSubDep3Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
	}

	public CFBamClearSubDep3Buff[] readBuffByClearSubDep2Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readBuffByClearSubDep2Idx() ";
		CFBamClearSubDep3Buff buff;
		ArrayList<CFBamClearSubDep3Buff> filteredList = new ArrayList<CFBamClearSubDep3Buff>();
		CFBamClearSubDep3Buff[] buffList = readDerivedByClearSubDep2Idx( Authorization,
			ClearSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a813" ) ) {
				filteredList.add( (CFBamClearSubDep3Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep3Buff[0] ) );
	}

	public CFBamClearSubDep3Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readBuffByUNameIdx() ";
		CFBamClearSubDep3Buff buff = readDerivedByUNameIdx( Authorization,
			ClearSubDep2Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a813" ) ) {
			return( (CFBamClearSubDep3Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific ClearSubDep3 buffer instances identified by the duplicate key ClearDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The ClearSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep3Buff[] pageBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep3 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ClearSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep3Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep3 buffer instances identified by the duplicate key ClearSubDep2Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ClearSubDep2Id	The ClearSubDep3 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep3Buff[] pageBuffByClearSubDep2Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearSubDep2Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateClearSubDep3( CFSecAuthorization Authorization,
		CFBamClearSubDep3Buff Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep3Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep3",
				"Existing record not found",
				"ClearSubDep3",
				pkey );
		}
		CFBamClearSubDep3ByClearSubDep2IdxKey existingKeyClearSubDep2Idx = schema.getFactoryClearSubDep3().newClearSubDep2IdxKey();
		existingKeyClearSubDep2Idx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );

		CFBamClearSubDep3ByClearSubDep2IdxKey newKeyClearSubDep2Idx = schema.getFactoryClearSubDep3().newClearSubDep2IdxKey();
		newKeyClearSubDep2Idx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );

		CFBamClearSubDep3ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearSubDep3().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamClearSubDep3ByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearSubDep3().newUNameIdxKey();
		newKeyUNameIdx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep3",
					"ClearSubDep3UNameIdx",
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
						"updateClearSubDep3",
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
				if( null == schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep3",
						"Container",
						"ClearSubDep2",
						"ClearSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamClearSubDep3Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearSubDep2Idx.get( existingKeyClearSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearSubDep2Idx.containsKey( newKeyClearSubDep2Idx ) ) {
			subdict = dictByClearSubDep2Idx.get( newKeyClearSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearSubDep3Buff >();
			dictByClearSubDep2Idx.put( newKeyClearSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteClearSubDep3( CFSecAuthorization Authorization,
		CFBamClearSubDep3Buff Buff )
	{
		final String S_ProcName = "CFBamRamClearSubDep3Table.deleteClearSubDep3() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep3Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep3",
				pkey );
		}
		CFBamClearSubDep3ByClearSubDep2IdxKey keyClearSubDep2Idx = schema.getFactoryClearSubDep3().newClearSubDep2IdxKey();
		keyClearSubDep2Idx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );

		CFBamClearSubDep3ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep3().newUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamClearSubDep3Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearSubDep2Idx.get( keyClearSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearSubDep3ByClearSubDep2Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep2Id )
	{
		CFBamClearSubDep3ByClearSubDep2IdxKey key = schema.getFactoryClearSubDep3().newClearSubDep2IdxKey();
		key.setRequiredClearSubDep2Id( argClearSubDep2Id );
		deleteClearSubDep3ByClearSubDep2Idx( Authorization, key );
	}

	public void deleteClearSubDep3ByClearSubDep2Idx( CFSecAuthorization Authorization,
		CFBamClearSubDep3ByClearSubDep2IdxKey argKey )
	{
		CFBamClearSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep3Buff> matchSet = new LinkedList<CFBamClearSubDep3Buff>();
		Iterator<CFBamClearSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep3( Authorization, cur );
		}
	}

	public void deleteClearSubDep3ByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep2Id,
		String argName )
	{
		CFBamClearSubDep3ByUNameIdxKey key = schema.getFactoryClearSubDep3().newUNameIdxKey();
		key.setRequiredClearSubDep2Id( argClearSubDep2Id );
		key.setRequiredName( argName );
		deleteClearSubDep3ByUNameIdx( Authorization, key );
	}

	public void deleteClearSubDep3ByUNameIdx( CFSecAuthorization Authorization,
		CFBamClearSubDep3ByUNameIdxKey argKey )
	{
		CFBamClearSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep3Buff> matchSet = new LinkedList<CFBamClearSubDep3Buff>();
		Iterator<CFBamClearSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep3( Authorization, cur );
		}
	}

	public void deleteClearSubDep3ByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep3ByClearDepIdx( Authorization, key );
	}

	public void deleteClearSubDep3ByClearDepIdx( CFSecAuthorization Authorization,
		CFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamClearSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep3Buff> matchSet = new LinkedList<CFBamClearSubDep3Buff>();
		Iterator<CFBamClearSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep3( Authorization, cur );
		}
	}

	public void deleteClearSubDep3ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep3ByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearSubDep3ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamClearSubDep3Buff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep3Buff> matchSet = new LinkedList<CFBamClearSubDep3Buff>();
		Iterator<CFBamClearSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep3( Authorization, cur );
		}
	}

	public void deleteClearSubDep3ByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearSubDep3ByIdIdx( Authorization, key );
	}

	public void deleteClearSubDep3ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamClearSubDep3Buff cur;
		LinkedList<CFBamClearSubDep3Buff> matchSet = new LinkedList<CFBamClearSubDep3Buff>();
		Iterator<CFBamClearSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep3( Authorization, cur );
		}
	}

	public void deleteClearSubDep3ByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep3ByTenantIdx( Authorization, key );
	}

	public void deleteClearSubDep3ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamClearSubDep3Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep3Buff> matchSet = new LinkedList<CFBamClearSubDep3Buff>();
		Iterator<CFBamClearSubDep3Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep3Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep3( Authorization, cur );
		}
	}
}
