
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
 *	CFBamRamClearSubDep2Table in-memory RAM DbIO implementation
 *	for ClearSubDep2.
 */
public class CFBamRamClearSubDep2Table
	implements ICFBamClearSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearSubDep2 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearSubDep2 >();
	private Map< CFBamBuffClearSubDep2ByClearSubDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep2 >> dictByClearSubDep1Idx
		= new HashMap< CFBamBuffClearSubDep2ByClearSubDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep2 >>();
	private Map< CFBamBuffClearSubDep2ByUNameIdxKey,
			CFBamBuffClearSubDep2 > dictByUNameIdx
		= new HashMap< CFBamBuffClearSubDep2ByUNameIdxKey,
			CFBamBuffClearSubDep2 >();

	public CFBamRamClearSubDep2Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearSubDep2( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2 Buff )
	{
		final String S_ProcName = "createClearSubDep2";
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey keyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		keyClearSubDep1Idx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdictClearSubDep1Idx;
		if( dictByClearSubDep1Idx.containsKey( keyClearSubDep1Idx ) ) {
			subdictClearSubDep1Idx = dictByClearSubDep1Idx.get( keyClearSubDep1Idx );
		}
		else {
			subdictClearSubDep1Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep2 >();
			dictByClearSubDep1Idx.put( keyClearSubDep1Idx, subdictClearSubDep1Idx );
		}
		subdictClearSubDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public ICFBamClearSubDep2 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerived";
		ICFBamClearSubDep2 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep2 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamClearSubDep2 buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep2[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep2.readAllDerived";
		ICFBamClearSubDep2[] retList = new ICFBamClearSubDep2[ dictByPKey.values().size() ];
		Iterator< ICFBamClearSubDep2 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamClearSubDep2[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep2 ) ) {
					filteredList.add( (ICFBamClearSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
		}
	}

	public ICFBamClearSubDep2[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep2 ) ) {
					filteredList.add( (ICFBamClearSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
		}
	}

	public ICFBamClearSubDep2[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep2 ) ) {
					filteredList.add( (ICFBamClearSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
		}
	}

	public ICFBamClearSubDep2[] readDerivedByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerivedByClearSubDep1Idx";
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey key = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		key.setRequiredClearSubDep1Id( ClearSubDep1Id );

		ICFBamClearSubDep2[] recArray;
		if( dictByClearSubDep1Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdictClearSubDep1Idx
				= dictByClearSubDep1Idx.get( key );
			recArray = new ICFBamClearSubDep2[ subdictClearSubDep1Idx.size() ];
			Iterator< ICFBamClearSubDep2 > iter = subdictClearSubDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdictClearSubDep1Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep2 >();
			dictByClearSubDep1Idx.put( key, subdictClearSubDep1Idx );
			recArray = new ICFBamClearSubDep2[0];
		}
		return( recArray );
	}

	public ICFBamClearSubDep2 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerivedByUNameIdx";
		CFBamBuffClearSubDep2ByUNameIdxKey key = schema.getFactoryClearSubDep2().newUNameIdxKey();
		key.setRequiredClearSubDep1Id( ClearSubDep1Id );
		key.setRequiredName( Name );

		ICFBamClearSubDep2 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep2 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		ICFBamClearSubDep2 buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep2 readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readBuff";
		ICFBamClearSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a812" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep2 lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamClearSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a812" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep2[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readAllBuff";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a812" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	public ICFBamClearSubDep2 readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamClearSubDep2 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (ICFBamClearSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearSubDep2[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	public ICFBamClearSubDep2[] readBuffByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	public ICFBamClearSubDep2[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	public ICFBamClearSubDep2[] readBuffByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readBuffByClearSubDep1Idx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByClearSubDep1Idx( Authorization,
			ClearSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a812" ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	public ICFBamClearSubDep2 readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readBuffByUNameIdx() ";
		ICFBamClearSubDep2 buff = readDerivedByUNameIdx( Authorization,
			ClearSubDep1Id,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a812" ) ) {
			return( (ICFBamClearSubDep2)buff );
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
	public ICFBamClearSubDep2[] pageBuffByClearDepIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearSubDep2[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearSubDep2[] pageBuffByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearSubDep1Idx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateClearSubDep2( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2 Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamClearSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep2",
				"Existing record not found",
				"ClearSubDep2",
				pkey );
		}
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey existingKeyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		existingKeyClearSubDep1Idx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByClearSubDep1IdxKey newKeyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		newKeyClearSubDep1Idx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearSubDep2ByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep2 >();
			dictByClearSubDep1Idx.put( newKeyClearSubDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteClearSubDep2( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2 Buff )
	{
		final String S_ProcName = "CFBamRamClearSubDep2Table.deleteClearSubDep2() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamClearSubDep2 existing = dictByPKey.get( pkey );
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
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey keyClearSubDep1Idx = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		keyClearSubDep1Idx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep2().newUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearSubDep1Idx.get( keyClearSubDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearSubDep2ByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep1Id )
	{
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey key = schema.getFactoryClearSubDep2().newClearSubDep1IdxKey();
		key.setRequiredClearSubDep1Id( argClearSubDep1Id );
		deleteClearSubDep2ByClearSubDep1Idx( Authorization, key );
	}

	public void deleteClearSubDep2ByClearSubDep1Idx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2ByClearSubDep1IdxKey argKey )
	{
		ICFBamClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep2> matchSet = new LinkedList<ICFBamClearSubDep2>();
		Iterator<ICFBamClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep1Id,
		String argName )
	{
		CFBamBuffClearSubDep2ByUNameIdxKey key = schema.getFactoryClearSubDep2().newUNameIdxKey();
		key.setRequiredClearSubDep1Id( argClearSubDep1Id );
		key.setRequiredName( argName );
		deleteClearSubDep2ByUNameIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2ByUNameIdxKey argKey )
	{
		ICFBamClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep2> matchSet = new LinkedList<ICFBamClearSubDep2>();
		Iterator<ICFBamClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep2ByClearDepIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		ICFBamClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep2> matchSet = new LinkedList<ICFBamClearSubDep2>();
		Iterator<ICFBamClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep2ByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		ICFBamClearSubDep2 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep2> matchSet = new LinkedList<ICFBamClearSubDep2>();
		Iterator<ICFBamClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearSubDep2ByIdIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamClearSubDep2 cur;
		LinkedList<ICFBamClearSubDep2> matchSet = new LinkedList<ICFBamClearSubDep2>();
		Iterator<ICFBamClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}

	public void deleteClearSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep2ByTenantIdx( Authorization, key );
	}

	public void deleteClearSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep2> matchSet = new LinkedList<ICFBamClearSubDep2>();
		Iterator<ICFBamClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep2( Authorization, cur );
		}
	}
}
