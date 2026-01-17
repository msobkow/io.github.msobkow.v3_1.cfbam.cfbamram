
// Description: Java 25 in-memory RAM DbIO implementation for ServerMethod.

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
 *	CFBamRamServerMethodTable in-memory RAM DbIO implementation
 *	for ServerMethod.
 */
public class CFBamRamServerMethodTable
	implements ICFBamServerMethodTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamServerMethodBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamServerMethodBuff >();
	private Map< CFBamServerMethodByUNameIdxKey,
			CFBamServerMethodBuff > dictByUNameIdx
		= new HashMap< CFBamServerMethodByUNameIdxKey,
			CFBamServerMethodBuff >();
	private Map< CFBamServerMethodByMethTableIdxKey,
				Map< CFBamScopePKey,
					CFBamServerMethodBuff >> dictByMethTableIdx
		= new HashMap< CFBamServerMethodByMethTableIdxKey,
				Map< CFBamScopePKey,
					CFBamServerMethodBuff >>();
	private Map< CFBamServerMethodByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamServerMethodBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamServerMethodByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamServerMethodBuff >>();

	public CFBamRamServerMethodTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createServerMethod( CFSecAuthorization Authorization,
		CFBamServerMethodBuff Buff )
	{
		final String S_ProcName = "createServerMethod";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerMethodByUNameIdxKey keyUNameIdx = schema.getFactoryServerMethod().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamServerMethodByMethTableIdxKey keyMethTableIdx = schema.getFactoryServerMethod().newMethTableIdxKey();
		keyMethTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamServerMethodByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ServerMethodUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ForTable",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamServerMethodBuff > subdictMethTableIdx;
		if( dictByMethTableIdx.containsKey( keyMethTableIdx ) ) {
			subdictMethTableIdx = dictByMethTableIdx.get( keyMethTableIdx );
		}
		else {
			subdictMethTableIdx = new HashMap< CFBamScopePKey, CFBamServerMethodBuff >();
			dictByMethTableIdx.put( keyMethTableIdx, subdictMethTableIdx );
		}
		subdictMethTableIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamServerMethodBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamServerMethodBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamServerMethodBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerMethodBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerMethodBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamServerMethodBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerMethodBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerMethod.readAllDerived";
		CFBamServerMethodBuff[] retList = new CFBamServerMethodBuff[ dictByPKey.values().size() ];
		Iterator< CFBamServerMethodBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamServerMethodBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamServerMethodBuff> filteredList = new ArrayList<CFBamServerMethodBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamServerMethodBuff ) ) {
					filteredList.add( (CFBamServerMethodBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamServerMethodBuff[0] ) );
		}
	}

	public CFBamServerMethodBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		CFBamServerMethodByUNameIdxKey key = schema.getFactoryServerMethod().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamServerMethodBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerMethodBuff[] readDerivedByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		CFBamServerMethodByMethTableIdxKey key = schema.getFactoryServerMethod().newMethTableIdxKey();
		key.setRequiredTableId( TableId );

		CFBamServerMethodBuff[] recArray;
		if( dictByMethTableIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamServerMethodBuff > subdictMethTableIdx
				= dictByMethTableIdx.get( key );
			recArray = new CFBamServerMethodBuff[ subdictMethTableIdx.size() ];
			Iterator< CFBamServerMethodBuff > iter = subdictMethTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamServerMethodBuff > subdictMethTableIdx
				= new HashMap< CFBamScopePKey, CFBamServerMethodBuff >();
			dictByMethTableIdx.put( key, subdictMethTableIdx );
			recArray = new CFBamServerMethodBuff[0];
		}
		return( recArray );
	}

	public CFBamServerMethodBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		CFBamServerMethodByDefSchemaIdxKey key = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamServerMethodBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamServerMethodBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamServerMethodBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamServerMethodBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamServerMethodBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamServerMethodBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamServerMethodBuff[0];
		}
		return( recArray );
	}

	public CFBamServerMethodBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamServerMethodBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerMethodBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuff";
		CFBamServerMethodBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a805" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerMethodBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamServerMethodBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a805" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamServerMethodBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerMethod.readAllBuff";
		CFBamServerMethodBuff buff;
		ArrayList<CFBamServerMethodBuff> filteredList = new ArrayList<CFBamServerMethodBuff>();
		CFBamServerMethodBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamServerMethodBuff[0] ) );
	}

	public CFBamServerMethodBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamServerMethodBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamServerMethodBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerMethodBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamServerMethodBuff buff;
		ArrayList<CFBamServerMethodBuff> filteredList = new ArrayList<CFBamServerMethodBuff>();
		CFBamServerMethodBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamServerMethodBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerMethodBuff[0] ) );
	}

	public CFBamServerMethodBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByUNameIdx() ";
		CFBamServerMethodBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
			return( (CFBamServerMethodBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamServerMethodBuff[] readBuffByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByMethTableIdx() ";
		CFBamServerMethodBuff buff;
		ArrayList<CFBamServerMethodBuff> filteredList = new ArrayList<CFBamServerMethodBuff>();
		CFBamServerMethodBuff[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerMethodBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerMethodBuff[0] ) );
	}

	public CFBamServerMethodBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readBuffByDefSchemaIdx() ";
		CFBamServerMethodBuff buff;
		ArrayList<CFBamServerMethodBuff> filteredList = new ArrayList<CFBamServerMethodBuff>();
		CFBamServerMethodBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a805" ) ) {
				filteredList.add( (CFBamServerMethodBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamServerMethodBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific ServerMethod buffer instances identified by the duplicate key MethTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The ServerMethod key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerMethodBuff[] pageBuffByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByMethTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerMethod buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ServerMethod key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamServerMethodBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateServerMethod( CFSecAuthorization Authorization,
		CFBamServerMethodBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerMethodBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerMethod",
				"Existing record not found",
				"ServerMethod",
				pkey );
		}
		CFBamServerMethodByUNameIdxKey existingKeyUNameIdx = schema.getFactoryServerMethod().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamServerMethodByUNameIdxKey newKeyUNameIdx = schema.getFactoryServerMethod().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamServerMethodByMethTableIdxKey existingKeyMethTableIdx = schema.getFactoryServerMethod().newMethTableIdxKey();
		existingKeyMethTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamServerMethodByMethTableIdxKey newKeyMethTableIdx = schema.getFactoryServerMethod().newMethTableIdxKey();
		newKeyMethTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamServerMethodByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamServerMethodByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateServerMethod",
					"ServerMethodUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerMethod",
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerMethod",
						"Container",
						"ForTable",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamServerMethodBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByMethTableIdx.get( existingKeyMethTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByMethTableIdx.containsKey( newKeyMethTableIdx ) ) {
			subdict = dictByMethTableIdx.get( newKeyMethTableIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamServerMethodBuff >();
			dictByMethTableIdx.put( newKeyMethTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamServerMethodBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteServerMethod( CFSecAuthorization Authorization,
		CFBamServerMethodBuff Buff )
	{
		final String S_ProcName = "CFBamRamServerMethodTable.deleteServerMethod() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamServerMethodBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerMethod",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckParams[] = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckParams.length > 0 ) {
			schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamServerMethodByUNameIdxKey keyUNameIdx = schema.getFactoryServerMethod().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamServerMethodByMethTableIdxKey keyMethTableIdx = schema.getFactoryServerMethod().newMethTableIdxKey();
		keyMethTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamServerMethodByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteServerMethod",
				"Superclass",
				"SuperClass",
				"ServerObjFunc",
				pkey );
		}

		if( schema.getTableServerProc().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteServerMethod",
				"Superclass",
				"SuperClass",
				"ServerProc",
				pkey );
		}

		if( schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteServerMethod",
				"Superclass",
				"SuperClass",
				"ServerListFunc",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamServerMethodBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByMethTableIdx.get( keyMethTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteServerMethodByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamServerMethodByUNameIdxKey key = schema.getFactoryServerMethod().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerMethodByUNameIdx( Authorization, key );
	}

	public void deleteServerMethodByUNameIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByUNameIdx";
		CFBamServerMethodBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerMethodBuff> matchSet = new LinkedList<CFBamServerMethodBuff>();
		Iterator<CFBamServerMethodBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerMethodBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (CFBamServerObjFuncBuff)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (CFBamServerProcBuff)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (CFBamServerListFuncBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ServerMethod must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteServerMethodByMethTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamServerMethodByMethTableIdxKey key = schema.getFactoryServerMethod().newMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerMethodByMethTableIdx( Authorization, key );
	}

	public void deleteServerMethodByMethTableIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByMethTableIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByMethTableIdx";
		CFBamServerMethodBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerMethodBuff> matchSet = new LinkedList<CFBamServerMethodBuff>();
		Iterator<CFBamServerMethodBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerMethodBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (CFBamServerObjFuncBuff)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (CFBamServerProcBuff)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (CFBamServerListFuncBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ServerMethod must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteServerMethodByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamServerMethodByDefSchemaIdxKey key = schema.getFactoryServerMethod().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerMethodByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerMethodByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamServerMethodByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByDefSchemaIdx";
		CFBamServerMethodBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerMethodBuff> matchSet = new LinkedList<CFBamServerMethodBuff>();
		Iterator<CFBamServerMethodBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerMethodBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (CFBamServerObjFuncBuff)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (CFBamServerProcBuff)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (CFBamServerListFuncBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ServerMethod must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteServerMethodByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteServerMethodByIdIdx( Authorization, key );
	}

	public void deleteServerMethodByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamServerMethodBuff cur;
		LinkedList<CFBamServerMethodBuff> matchSet = new LinkedList<CFBamServerMethodBuff>();
		Iterator<CFBamServerMethodBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerMethodBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (CFBamServerObjFuncBuff)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (CFBamServerProcBuff)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (CFBamServerListFuncBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ServerMethod must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteServerMethodByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerMethodByTenantIdx( Authorization, key );
	}

	public void deleteServerMethodByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByTenantIdx";
		CFBamServerMethodBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamServerMethodBuff> matchSet = new LinkedList<CFBamServerMethodBuff>();
		Iterator<CFBamServerMethodBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamServerMethodBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (CFBamServerObjFuncBuff)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (CFBamServerProcBuff)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (CFBamServerListFuncBuff)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of ServerMethod must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
