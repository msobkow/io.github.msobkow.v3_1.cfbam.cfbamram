
// Description: Java 25 in-memory RAM DbIO implementation for Index.

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
 *	CFBamRamIndexTable in-memory RAM DbIO implementation
 *	for Index.
 */
public class CFBamRamIndexTable
	implements ICFBamIndexTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamIndexBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamIndexBuff >();
	private Map< CFBamIndexByUNameIdxKey,
			CFBamIndexBuff > dictByUNameIdx
		= new HashMap< CFBamIndexByUNameIdxKey,
			CFBamIndexBuff >();
	private Map< CFBamIndexByIdxTableIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >> dictByIdxTableIdx
		= new HashMap< CFBamIndexByIdxTableIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >>();
	private Map< CFBamIndexByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamIndexByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >>();

	public CFBamRamIndexTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createIndex( CFSecAuthorization Authorization,
		CFBamIndexBuff Buff )
	{
		final String S_ProcName = "createIndex";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexByUNameIdxKey keyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamIndexByIdxTableIdxKey keyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamIndexByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexUNameIdx",
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
						"Table",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamIndexBuff > subdictIdxTableIdx;
		if( dictByIdxTableIdx.containsKey( keyIdxTableIdx ) ) {
			subdictIdxTableIdx = dictByIdxTableIdx.get( keyIdxTableIdx );
		}
		else {
			subdictIdxTableIdx = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIdxTableIdx.put( keyIdxTableIdx, subdictIdxTableIdx );
		}
		subdictIdxTableIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamIndexBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamIndexBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndex.readAllDerived";
		CFBamIndexBuff[] retList = new CFBamIndexBuff[ dictByPKey.values().size() ];
		Iterator< CFBamIndexBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamIndexBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamIndexBuff ) ) {
					filteredList.add( (CFBamIndexBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamIndexBuff[0] ) );
		}
	}

	public CFBamIndexBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByUNameIdx";
		CFBamIndexByUNameIdxKey key = schema.getFactoryIndex().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamIndexBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff[] readDerivedByIdxTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIdxTableIdx";
		CFBamIndexByIdxTableIdxKey key = schema.getFactoryIndex().newIdxTableIdxKey();
		key.setRequiredTableId( TableId );

		CFBamIndexBuff[] recArray;
		if( dictByIdxTableIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictIdxTableIdx
				= dictByIdxTableIdx.get( key );
			recArray = new CFBamIndexBuff[ subdictIdxTableIdx.size() ];
			Iterator< CFBamIndexBuff > iter = subdictIdxTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictIdxTableIdx
				= new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIdxTableIdx.put( key, subdictIdxTableIdx );
			recArray = new CFBamIndexBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByDefSchemaIdx";
		CFBamIndexByDefSchemaIdxKey key = schema.getFactoryIndex().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamIndexBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamIndexBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamIndexBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamIndexBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamIndexBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readBuff";
		CFBamIndexBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a821" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamIndexBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a821" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndex.readAllBuff";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamIndexBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamIndexBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByUNameIdx() ";
		CFBamIndexBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
			return( (CFBamIndexBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexBuff[] readBuffByIdxTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByIdxTableIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByIdxTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByDefSchemaIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific Index buffer instances identified by the duplicate key IdxTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The Index key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamIndexBuff[] pageBuffByIdxTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByIdxTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Index buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The Index key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamIndexBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateIndex( CFSecAuthorization Authorization,
		CFBamIndexBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndex",
				"Existing record not found",
				"Index",
				pkey );
		}
		CFBamIndexByUNameIdxKey existingKeyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamIndexByUNameIdxKey newKeyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamIndexByIdxTableIdxKey existingKeyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		existingKeyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamIndexByIdxTableIdxKey newKeyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		newKeyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamIndexByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamIndexByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndex",
					"IndexUNameIdx",
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
						"updateIndex",
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
						"updateIndex",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamIndexBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByIdxTableIdx.get( existingKeyIdxTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxTableIdx.containsKey( newKeyIdxTableIdx ) ) {
			subdict = dictByIdxTableIdx.get( newKeyIdxTableIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIdxTableIdx.put( newKeyIdxTableIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteIndex( CFSecAuthorization Authorization,
		CFBamIndexBuff Buff )
	{
		final String S_ProcName = "CFBamRamIndexTable.deleteIndex() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndex",
				pkey );
		}
		CFBamIndexColBuff buffDelIndexRefRelFromCols;
		CFBamIndexColBuff arrDelIndexRefRelFromCols[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelIndexRefRelFromCols = 0; idxDelIndexRefRelFromCols < arrDelIndexRefRelFromCols.length; idxDelIndexRefRelFromCols++ ) {
			buffDelIndexRefRelFromCols = arrDelIndexRefRelFromCols[idxDelIndexRefRelFromCols];
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffDelIndexRefRelFromCols.getRequiredId() );
		}
		CFBamIndexColBuff buffDelIndexRefRelToCols;
		CFBamIndexColBuff arrDelIndexRefRelToCols[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelIndexRefRelToCols = 0; idxDelIndexRefRelToCols < arrDelIndexRefRelToCols.length; idxDelIndexRefRelToCols++ ) {
			buffDelIndexRefRelToCols = arrDelIndexRefRelToCols[idxDelIndexRefRelToCols];
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffDelIndexRefRelToCols.getRequiredId() );
		}
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						existing.getRequiredId() );
		CFBamIndexByUNameIdxKey keyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamIndexByIdxTableIdxKey keyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamIndexByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableRelation().readDerivedByFromKeyIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndex",
				"Lookup",
				"FromIndex",
				"Relation",
				pkey );
		}

		if( schema.getTableRelation().readDerivedByToKeyIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndex",
				"Lookup",
				"ToIndex",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamIndexBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIdxTableIdx.get( keyIdxTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteIndexByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamIndexByUNameIdxKey key = schema.getFactoryIndex().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteIndexByUNameIdx( Authorization, key );
	}

	public void deleteIndexByUNameIdx( CFSecAuthorization Authorization,
		CFBamIndexByUNameIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIdxTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamIndexByIdxTableIdxKey key = schema.getFactoryIndex().newIdxTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteIndexByIdxTableIdx( Authorization, key );
	}

	public void deleteIndexByIdxTableIdx( CFSecAuthorization Authorization,
		CFBamIndexByIdxTableIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamIndexByDefSchemaIdxKey key = schema.getFactoryIndex().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexByDefSchemaIdx( Authorization, key );
	}

	public void deleteIndexByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamIndexByDefSchemaIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteIndexByIdIdx( Authorization, key );
	}

	public void deleteIndexByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamIndexBuff cur;
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexByTenantIdx( Authorization, key );
	}

	public void deleteIndexByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}
}
