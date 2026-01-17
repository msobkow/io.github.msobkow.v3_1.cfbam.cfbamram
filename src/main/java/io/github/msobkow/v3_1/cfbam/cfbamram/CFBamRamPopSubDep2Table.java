
// Description: Java 25 in-memory RAM DbIO implementation for PopSubDep2.

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
 *	CFBamRamPopSubDep2Table in-memory RAM DbIO implementation
 *	for PopSubDep2.
 */
public class CFBamRamPopSubDep2Table
	implements ICFBamPopSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamPopSubDep2Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamPopSubDep2Buff >();
	private Map< CFBamPopSubDep2ByPopSubDep1IdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep2Buff >> dictByPopSubDep1Idx
		= new HashMap< CFBamPopSubDep2ByPopSubDep1IdxKey,
				Map< CFBamScopePKey,
					CFBamPopSubDep2Buff >>();
	private Map< CFBamPopSubDep2ByUNameIdxKey,
			CFBamPopSubDep2Buff > dictByUNameIdx
		= new HashMap< CFBamPopSubDep2ByUNameIdxKey,
			CFBamPopSubDep2Buff >();

	public CFBamRamPopSubDep2Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createPopSubDep2( CFSecAuthorization Authorization,
		CFBamPopSubDep2Buff Buff )
	{
		final String S_ProcName = "createPopSubDep2";
		schema.getTablePopDep().createPopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep2ByPopSubDep1IdxKey keyPopSubDep1Idx = schema.getFactoryPopSubDep2().newPopSubDep1IdxKey();
		keyPopSubDep1Idx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );

		CFBamPopSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep2UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"PopSubDep1",
						"PopSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamPopSubDep2Buff > subdictPopSubDep1Idx;
		if( dictByPopSubDep1Idx.containsKey( keyPopSubDep1Idx ) ) {
			subdictPopSubDep1Idx = dictByPopSubDep1Idx.get( keyPopSubDep1Idx );
		}
		else {
			subdictPopSubDep1Idx = new HashMap< CFBamScopePKey, CFBamPopSubDep2Buff >();
			dictByPopSubDep1Idx.put( keyPopSubDep1Idx, subdictPopSubDep1Idx );
		}
		subdictPopSubDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamPopSubDep2Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep2Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamPopSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep2Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep2.readAllDerived";
		CFBamPopSubDep2Buff[] retList = new CFBamPopSubDep2Buff[ dictByPKey.values().size() ];
		Iterator< CFBamPopSubDep2Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamPopSubDep2Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep2Buff ) ) {
					filteredList.add( (CFBamPopSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
		}
	}

	public CFBamPopSubDep2Buff[] readDerivedByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		CFBamPopDepBuff buffList[] = schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamPopDepBuff buff;
			ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep2Buff ) ) {
					filteredList.add( (CFBamPopSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
		}
	}

	public CFBamPopSubDep2Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		CFBamPopDepBuff buffList[] = schema.getTablePopDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamPopDepBuff buff;
			ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamPopSubDep2Buff ) ) {
					filteredList.add( (CFBamPopSubDep2Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
		}
	}

	public CFBamPopSubDep2Buff[] readDerivedByPopSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerivedByPopSubDep1Idx";
		CFBamPopSubDep2ByPopSubDep1IdxKey key = schema.getFactoryPopSubDep2().newPopSubDep1IdxKey();
		key.setRequiredPopSubDep1Id( PopSubDep1Id );

		CFBamPopSubDep2Buff[] recArray;
		if( dictByPopSubDep1Idx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamPopSubDep2Buff > subdictPopSubDep1Idx
				= dictByPopSubDep1Idx.get( key );
			recArray = new CFBamPopSubDep2Buff[ subdictPopSubDep1Idx.size() ];
			Iterator< CFBamPopSubDep2Buff > iter = subdictPopSubDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamPopSubDep2Buff > subdictPopSubDep1Idx
				= new HashMap< CFBamScopePKey, CFBamPopSubDep2Buff >();
			dictByPopSubDep1Idx.put( key, subdictPopSubDep1Idx );
			recArray = new CFBamPopSubDep2Buff[0];
		}
		return( recArray );
	}

	public CFBamPopSubDep2Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerivedByUNameIdx";
		CFBamPopSubDep2ByUNameIdxKey key = schema.getFactoryPopSubDep2().newUNameIdxKey();
		key.setRequiredPopSubDep1Id( PopSubDep1Id );
		key.setRequiredName( Name );

		CFBamPopSubDep2Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep2Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamPopSubDep2Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep2Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readBuff";
		CFBamPopSubDep2Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a832" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep2Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamPopSubDep2Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a832" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamPopSubDep2Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readAllBuff";
		CFBamPopSubDep2Buff buff;
		ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
		CFBamPopSubDep2Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a832" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
	}

	public CFBamPopSubDep2Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamPopSubDep2Buff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamPopSubDep2Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamPopSubDep2Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamPopSubDep2Buff buff;
		ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
		CFBamPopSubDep2Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamPopSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
	}

	public CFBamPopSubDep2Buff[] readBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByRelationIdx() ";
		CFBamPopSubDep2Buff buff;
		ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
		CFBamPopSubDep2Buff[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
	}

	public CFBamPopSubDep2Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readBuffByDefSchemaIdx() ";
		CFBamPopSubDep2Buff buff;
		ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
		CFBamPopSubDep2Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a830" ) ) {
				filteredList.add( (CFBamPopSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
	}

	public CFBamPopSubDep2Buff[] readBuffByPopSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readBuffByPopSubDep1Idx() ";
		CFBamPopSubDep2Buff buff;
		ArrayList<CFBamPopSubDep2Buff> filteredList = new ArrayList<CFBamPopSubDep2Buff>();
		CFBamPopSubDep2Buff[] buffList = readDerivedByPopSubDep1Idx( Authorization,
			PopSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a832" ) ) {
				filteredList.add( (CFBamPopSubDep2Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamPopSubDep2Buff[0] ) );
	}

	public CFBamPopSubDep2Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readBuffByUNameIdx() ";
		CFBamPopSubDep2Buff buff = readDerivedByUNameIdx( Authorization,
			PopSubDep1Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a832" ) ) {
			return( (CFBamPopSubDep2Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific PopSubDep2 buffer instances identified by the duplicate key RelationIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The PopSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep2Buff[] pageBuffByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRelationIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep2 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The PopSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep2Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific PopSubDep2 buffer instances identified by the duplicate key PopSubDep1Idx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PopSubDep1Id	The PopSubDep2 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamPopSubDep2Buff[] pageBuffByPopSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPopSubDep1Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updatePopSubDep2( CFSecAuthorization Authorization,
		CFBamPopSubDep2Buff Buff )
	{
		schema.getTablePopDep().updatePopDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep2Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep2",
				"Existing record not found",
				"PopSubDep2",
				pkey );
		}
		CFBamPopSubDep2ByPopSubDep1IdxKey existingKeyPopSubDep1Idx = schema.getFactoryPopSubDep2().newPopSubDep1IdxKey();
		existingKeyPopSubDep1Idx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );

		CFBamPopSubDep2ByPopSubDep1IdxKey newKeyPopSubDep1Idx = schema.getFactoryPopSubDep2().newPopSubDep1IdxKey();
		newKeyPopSubDep1Idx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );

		CFBamPopSubDep2ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryPopSubDep2().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamPopSubDep2ByUNameIdxKey newKeyUNameIdx = schema.getFactoryPopSubDep2().newUNameIdxKey();
		newKeyUNameIdx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep2",
					"PopSubDep2UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep2",
						"Superclass",
						"SuperClass",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep2",
						"Container",
						"PopSubDep1",
						"PopSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamPopSubDep2Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopSubDep1Idx.get( existingKeyPopSubDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopSubDep1Idx.containsKey( newKeyPopSubDep1Idx ) ) {
			subdict = dictByPopSubDep1Idx.get( newKeyPopSubDep1Idx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamPopSubDep2Buff >();
			dictByPopSubDep1Idx.put( newKeyPopSubDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deletePopSubDep2( CFSecAuthorization Authorization,
		CFBamPopSubDep2Buff Buff )
	{
		final String S_ProcName = "CFBamRamPopSubDep2Table.deletePopSubDep2() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamPopSubDep2Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep3().readDerivedByPopSubDep2Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep3().deletePopSubDep3ByPopSubDep2Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamPopSubDep2ByPopSubDep1IdxKey keyPopSubDep1Idx = schema.getFactoryPopSubDep2().newPopSubDep1IdxKey();
		keyPopSubDep1Idx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );

		CFBamPopSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryPopSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamPopSubDep2Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopSubDep1Idx.get( keyPopSubDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	public void deletePopSubDep2ByPopSubDep1Idx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep1Id )
	{
		CFBamPopSubDep2ByPopSubDep1IdxKey key = schema.getFactoryPopSubDep2().newPopSubDep1IdxKey();
		key.setRequiredPopSubDep1Id( argPopSubDep1Id );
		deletePopSubDep2ByPopSubDep1Idx( Authorization, key );
	}

	public void deletePopSubDep2ByPopSubDep1Idx( CFSecAuthorization Authorization,
		CFBamPopSubDep2ByPopSubDep1IdxKey argKey )
	{
		CFBamPopSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep2Buff> matchSet = new LinkedList<CFBamPopSubDep2Buff>();
		Iterator<CFBamPopSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep2( Authorization, cur );
		}
	}

	public void deletePopSubDep2ByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep1Id,
		String argName )
	{
		CFBamPopSubDep2ByUNameIdxKey key = schema.getFactoryPopSubDep2().newUNameIdxKey();
		key.setRequiredPopSubDep1Id( argPopSubDep1Id );
		key.setRequiredName( argName );
		deletePopSubDep2ByUNameIdx( Authorization, key );
	}

	public void deletePopSubDep2ByUNameIdx( CFSecAuthorization Authorization,
		CFBamPopSubDep2ByUNameIdxKey argKey )
	{
		CFBamPopSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep2Buff> matchSet = new LinkedList<CFBamPopSubDep2Buff>();
		Iterator<CFBamPopSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep2( Authorization, cur );
		}
	}

	public void deletePopSubDep2ByRelationIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamPopDepByRelationIdxKey key = schema.getFactoryPopDep().newRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep2ByRelationIdx( Authorization, key );
	}

	public void deletePopSubDep2ByRelationIdx( CFSecAuthorization Authorization,
		CFBamPopDepByRelationIdxKey argKey )
	{
		CFBamPopSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep2Buff> matchSet = new LinkedList<CFBamPopSubDep2Buff>();
		Iterator<CFBamPopSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep2( Authorization, cur );
		}
	}

	public void deletePopSubDep2ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamPopDepByDefSchemaIdxKey key = schema.getFactoryPopDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep2ByDefSchemaIdx( Authorization, key );
	}

	public void deletePopSubDep2ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamPopSubDep2Buff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep2Buff> matchSet = new LinkedList<CFBamPopSubDep2Buff>();
		Iterator<CFBamPopSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep2( Authorization, cur );
		}
	}

	public void deletePopSubDep2ByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deletePopSubDep2ByIdIdx( Authorization, key );
	}

	public void deletePopSubDep2ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamPopSubDep2Buff cur;
		LinkedList<CFBamPopSubDep2Buff> matchSet = new LinkedList<CFBamPopSubDep2Buff>();
		Iterator<CFBamPopSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep2( Authorization, cur );
		}
	}

	public void deletePopSubDep2ByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep2ByTenantIdx( Authorization, key );
	}

	public void deletePopSubDep2ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamPopSubDep2Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamPopSubDep2Buff> matchSet = new LinkedList<CFBamPopSubDep2Buff>();
		Iterator<CFBamPopSubDep2Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamPopSubDep2Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deletePopSubDep2( Authorization, cur );
		}
	}
}
