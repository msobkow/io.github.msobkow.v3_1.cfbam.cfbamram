
// Description: Java 25 in-memory RAM DbIO implementation for ClearSubDep1.

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
 *	CFBamRamClearSubDep1Table in-memory RAM DbIO implementation
 *	for ClearSubDep1.
 */
public class CFBamRamClearSubDep1Table
	implements ICFBamClearSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamClearSubDep1Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamClearSubDep1Buff >();
	private Map< CFBamClearSubDep1ByClearTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep1Buff >> dictByClearTopDepIdx
		= new HashMap< CFBamClearSubDep1ByClearTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep1Buff >>();
	private Map< CFBamClearSubDep1ByUNameIdxKey,
			CFBamClearSubDep1Buff > dictByUNameIdx
		= new HashMap< CFBamClearSubDep1ByUNameIdxKey,
			CFBamClearSubDep1Buff >();

	public CFBamRamClearSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearSubDep1( CFSecAuthorization Authorization,
		CFBamClearSubDep1Buff Buff )
	{
		final String S_ProcName = "createClearSubDep1";
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep1UNameIdx",
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
				if( null == schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ClearTopDep",
						"ClearTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdictClearTopDepIdx;
		if( dictByClearTopDepIdx.containsKey( keyClearTopDepIdx ) ) {
			subdictClearTopDepIdx = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		}
		else {
			subdictClearTopDepIdx = new HashMap< CFBamScopePKey, CFBamClearSubDep1Buff >();
			dictByClearTopDepIdx.put( keyClearTopDepIdx, subdictClearTopDepIdx );
		}
		subdictClearTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamClearSubDep1Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep1.readAllDerived";
		CFBamClearSubDep1Buff[] retList = new CFBamClearSubDep1Buff[ dictByPKey.values().size() ];
		Iterator< CFBamClearSubDep1Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamClearSubDep1Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep1Buff ) ) {
					filteredList.add( (CFBamClearSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
		}
	}

	public CFBamClearSubDep1Buff[] readDerivedByClearDepIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep1Buff ) ) {
					filteredList.add( (CFBamClearSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
		}
	}

	public CFBamClearSubDep1Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep1Buff ) ) {
					filteredList.add( (CFBamClearSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
		}
	}

	public CFBamClearSubDep1Buff[] readDerivedByClearTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByClearTopDepIdx";
		CFBamClearSubDep1ByClearTopDepIdxKey key = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		key.setRequiredClearTopDepId( ClearTopDepId );

		CFBamClearSubDep1Buff[] recArray;
		if( dictByClearTopDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdictClearTopDepIdx
				= dictByClearTopDepIdx.get( key );
			recArray = new CFBamClearSubDep1Buff[ subdictClearTopDepIdx.size() ];
			Iterator< CFBamClearSubDep1Buff > iter = subdictClearTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdictClearTopDepIdx
				= new HashMap< CFBamScopePKey, CFBamClearSubDep1Buff >();
			dictByClearTopDepIdx.put( key, subdictClearTopDepIdx );
			recArray = new CFBamClearSubDep1Buff[0];
		}
		return( recArray );
	}

	public CFBamClearSubDep1Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByUNameIdx";
		CFBamClearSubDep1ByUNameIdxKey key = schema.getFactoryClearSubDep1().newUNameIdxKey();
		key.setRequiredClearTopDepId( ClearTopDepId );
		key.setRequiredName( Name );

		CFBamClearSubDep1Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamClearSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuff";
		CFBamClearSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a811" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamClearSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a811" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readAllBuff";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a811" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamClearSubDep1Buff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamClearSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearSubDep1Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff[] readBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff[] readBuffByClearTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuffByClearTopDepIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByClearTopDepIdx( Authorization,
			ClearTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a811" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuffByUNameIdx() ";
		CFBamClearSubDep1Buff buff = readDerivedByUNameIdx( Authorization,
			ClearTopDepId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a811" ) ) {
			return( (CFBamClearSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific ClearSubDep1 buffer instances identified by the duplicate key ClearDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep1Buff[] pageBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep1 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep1Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep1 buffer instances identified by the duplicate key ClearTopDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ClearTopDepId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep1Buff[] pageBuffByClearTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearTopDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateClearSubDep1( CFSecAuthorization Authorization,
		CFBamClearSubDep1Buff Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep1",
				"Existing record not found",
				"ClearSubDep1",
				pkey );
		}
		CFBamClearSubDep1ByClearTopDepIdxKey existingKeyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		existingKeyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByClearTopDepIdxKey newKeyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		newKeyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamClearSubDep1ByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		newKeyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep1",
					"ClearSubDep1UNameIdx",
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
						"updateClearSubDep1",
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
				if( null == schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep1",
						"Container",
						"ClearTopDep",
						"ClearTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearTopDepIdx.get( existingKeyClearTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearTopDepIdx.containsKey( newKeyClearTopDepIdx ) ) {
			subdict = dictByClearTopDepIdx.get( newKeyClearTopDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearSubDep1Buff >();
			dictByClearTopDepIdx.put( newKeyClearTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteClearSubDep1( CFSecAuthorization Authorization,
		CFBamClearSubDep1Buff Buff )
	{
		final String S_ProcName = "CFBamRamClearSubDep1Table.deleteClearSubDep1() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep2().readDerivedByClearSubDep1Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep2().deleteClearSubDep2ByClearSubDep1Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearSubDep1ByClearTopDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearTopDepId )
	{
		CFBamClearSubDep1ByClearTopDepIdxKey key = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		key.setRequiredClearTopDepId( argClearTopDepId );
		deleteClearSubDep1ByClearTopDepIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByClearTopDepIdx( CFSecAuthorization Authorization,
		CFBamClearSubDep1ByClearTopDepIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearTopDepId,
		String argName )
	{
		CFBamClearSubDep1ByUNameIdxKey key = schema.getFactoryClearSubDep1().newUNameIdxKey();
		key.setRequiredClearTopDepId( argClearTopDepId );
		key.setRequiredName( argName );
		deleteClearSubDep1ByUNameIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFBamClearSubDep1ByUNameIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep1ByClearDepIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByClearDepIdx( CFSecAuthorization Authorization,
		CFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep1ByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearSubDep1ByIdIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamClearSubDep1Buff cur;
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep1ByTenantIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}
}
