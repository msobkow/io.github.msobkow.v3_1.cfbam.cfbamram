
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
 *	CFBamRamClearSubDep1Table in-memory RAM DbIO implementation
 *	for ClearSubDep1.
 */
public class CFBamRamClearSubDep1Table
	implements ICFBamClearSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearSubDep1 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearSubDep1 >();
	private Map< CFBamBuffClearSubDep1ByClearTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep1 >> dictByClearTopDepIdx
		= new HashMap< CFBamBuffClearSubDep1ByClearTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep1 >>();
	private Map< CFBamBuffClearSubDep1ByUNameIdxKey,
			CFBamBuffClearSubDep1 > dictByUNameIdx
		= new HashMap< CFBamBuffClearSubDep1ByUNameIdxKey,
			CFBamBuffClearSubDep1 >();

	public CFBamRamClearSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamClearSubDep1 createClearSubDep1( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1 Buff )
	{
		final String S_ProcName = "createClearSubDep1";
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getFactoryClearSubDep1().newByClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getFactoryClearSubDep1().newByUNameIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdictClearTopDepIdx;
		if( dictByClearTopDepIdx.containsKey( keyClearTopDepIdx ) ) {
			subdictClearTopDepIdx = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		}
		else {
			subdictClearTopDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep1 >();
			dictByClearTopDepIdx.put( keyClearTopDepIdx, subdictClearTopDepIdx );
		}
		subdictClearTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		return( Buff );
	}

	public ICFBamClearSubDep1 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		ICFBamClearSubDep1 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep1 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		ICFBamClearSubDep1 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep1[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep1.readAllDerived";
		ICFBamClearSubDep1[] retList = new ICFBamClearSubDep1[ dictByPKey.values().size() ];
		Iterator< ICFBamClearSubDep1 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamClearSubDep1[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep1 ) ) {
					filteredList.add( (ICFBamClearSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
		}
	}

	public ICFBamClearSubDep1[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep1 ) ) {
					filteredList.add( (ICFBamClearSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
		}
	}

	public ICFBamClearSubDep1[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep1 ) ) {
					filteredList.add( (ICFBamClearSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
		}
	}

	public ICFBamClearSubDep1[] readDerivedByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByClearTopDepIdx";
		CFBamBuffClearSubDep1ByClearTopDepIdxKey key = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getFactoryClearSubDep1().newByClearTopDepIdxKey();
		key.setRequiredClearTopDepId( ClearTopDepId );

		ICFBamClearSubDep1[] recArray;
		if( dictByClearTopDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdictClearTopDepIdx
				= dictByClearTopDepIdx.get( key );
			recArray = new ICFBamClearSubDep1[ subdictClearTopDepIdx.size() ];
			Iterator< ICFBamClearSubDep1 > iter = subdictClearTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdictClearTopDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep1 >();
			dictByClearTopDepIdx.put( key, subdictClearTopDepIdx );
			recArray = new ICFBamClearSubDep1[0];
		}
		return( recArray );
	}

	public ICFBamClearSubDep1 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByUNameIdx";
		CFBamBuffClearSubDep1ByUNameIdxKey key = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getFactoryClearSubDep1().newByUNameIdxKey();
		key.setRequiredClearTopDepId( ClearTopDepId );
		key.setRequiredName( Name );

		ICFBamClearSubDep1 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep1 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamClearSubDep1 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep1 readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuff";
		ICFBamClearSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep1 lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamClearSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearSubDep1[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readAllBuff";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep1.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	public ICFBamClearSubDep1 readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamClearSubDep1 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearSubDep1[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	public ICFBamClearSubDep1[] readBuffByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	public ICFBamClearSubDep1[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	public ICFBamClearSubDep1[] readBuffByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuffByClearTopDepIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByClearTopDepIdx( Authorization,
			ClearTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep1.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	public ICFBamClearSubDep1 readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuffByUNameIdx() ";
		ICFBamClearSubDep1 buff = readDerivedByUNameIdx( Authorization,
			ClearTopDepId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep1.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep1)buff );
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
	public ICFBamClearSubDep1[] pageBuffByClearDepIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearSubDep1[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearSubDep1[] pageBuffByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearTopDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamClearSubDep1 updateClearSubDep1( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1 Buff )
	{
		ICFBamClearSubDep1 repl = schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		if (repl != Buff) {
			throw new CFLibInvalidStateException(getClass(), S_ProcName, "repl != Buff", "repl != Buff");
		}
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamClearSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep1",
				"Existing record not found",
				"ClearSubDep1",
				pkey );
		}
		CFBamBuffClearSubDep1ByClearTopDepIdxKey existingKeyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getFactoryClearSubDep1().newByClearTopDepIdxKey();
		existingKeyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByClearTopDepIdxKey newKeyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getFactoryClearSubDep1().newByClearTopDepIdxKey();
		newKeyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getFactoryClearSubDep1().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearSubDep1ByUNameIdxKey newKeyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getFactoryClearSubDep1().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep1",
					"ClearSubDep1UNameIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep1 >();
			dictByClearTopDepIdx.put( newKeyClearTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	public void deleteClearSubDep1( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1 Buff )
	{
		final String S_ProcName = "CFBamRamClearSubDep1Table.deleteClearSubDep1() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamClearSubDep1 existing = dictByPKey.get( pkey );
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
		CFBamBuffClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getFactoryClearSubDep1().newByClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getFactoryClearSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearSubDep1ByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearTopDepId )
	{
		CFBamBuffClearSubDep1ByClearTopDepIdxKey key = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getFactoryClearSubDep1().newByClearTopDepIdxKey();
		key.setRequiredClearTopDepId( argClearTopDepId );
		deleteClearSubDep1ByClearTopDepIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByClearTopDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1ByClearTopDepIdxKey argKey )
	{
		ICFBamClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep1> matchSet = new LinkedList<ICFBamClearSubDep1>();
		Iterator<ICFBamClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearTopDepId,
		String argName )
	{
		CFBamBuffClearSubDep1ByUNameIdxKey key = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getFactoryClearSubDep1().newByUNameIdxKey();
		key.setRequiredClearTopDepId( argClearTopDepId );
		key.setRequiredName( argName );
		deleteClearSubDep1ByUNameIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1ByUNameIdxKey argKey )
	{
		ICFBamClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep1> matchSet = new LinkedList<ICFBamClearSubDep1>();
		Iterator<ICFBamClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep1ByClearDepIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		ICFBamClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep1> matchSet = new LinkedList<ICFBamClearSubDep1>();
		Iterator<ICFBamClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep1ByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		ICFBamClearSubDep1 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep1> matchSet = new LinkedList<ICFBamClearSubDep1>();
		Iterator<ICFBamClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamClearSubDep1 cur;
		LinkedList<ICFBamClearSubDep1> matchSet = new LinkedList<ICFBamClearSubDep1>();
		Iterator<ICFBamClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep1ByTenantIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearSubDep1> matchSet = new LinkedList<ICFBamClearSubDep1>();
		Iterator<ICFBamClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}
}
