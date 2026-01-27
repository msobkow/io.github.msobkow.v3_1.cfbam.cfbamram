
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
 *	CFBamRamIndexTable in-memory RAM DbIO implementation
 *	for Index.
 */
public class CFBamRamIndexTable
	implements ICFBamIndexTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffIndex > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffIndex >();
	private Map< CFBamBuffIndexByUNameIdxKey,
			CFBamBuffIndex > dictByUNameIdx
		= new HashMap< CFBamBuffIndexByUNameIdxKey,
			CFBamBuffIndex >();
	private Map< CFBamBuffIndexByIdxTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >> dictByIdxTableIdx
		= new HashMap< CFBamBuffIndexByIdxTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >>();
	private Map< CFBamBuffIndexByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffIndexByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >>();

	public CFBamRamIndexTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamIndex createIndex( ICFSecAuthorization Authorization,
		ICFBamIndex iBuff )
	{
		final String S_ProcName = "createIndex";
		
		CFBamBuffIndex Buff = (CFBamBuffIndex)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffIndexByUNameIdxKey keyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getFactoryIndex().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexByIdxTableIdxKey keyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getFactoryIndex().newByIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffIndexByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getFactoryIndex().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexUNameIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTableIdx;
		if( dictByIdxTableIdx.containsKey( keyIdxTableIdx ) ) {
			subdictIdxTableIdx = dictByIdxTableIdx.get( keyIdxTableIdx );
		}
		else {
			subdictIdxTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTableIdx.put( keyIdxTableIdx, subdictIdxTableIdx );
		}
		subdictIdxTableIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamIndex.CLASS_CODE) {
				CFBamBuffIndex retbuff = ((CFBamBuffIndex)(schema.getFactoryIndex().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-create-buff-cloning-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamIndex readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		ICFBamIndex buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndex lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		ICFBamIndex buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndex[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndex.readAllDerived";
		ICFBamIndex[] retList = new ICFBamIndex[ dictByPKey.values().size() ];
		Iterator< ICFBamIndex > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamIndex[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamIndex ) ) {
					filteredList.add( (ICFBamIndex)buff );
				}
			}
			return( filteredList.toArray( new ICFBamIndex[0] ) );
		}
	}

	public ICFBamIndex readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByUNameIdx";
		CFBamBuffIndexByUNameIdxKey key = (CFBamBuffIndexByUNameIdxKey)schema.getFactoryIndex().newByUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		ICFBamIndex buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndex[] readDerivedByIdxTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIdxTableIdx";
		CFBamBuffIndexByIdxTableIdxKey key = (CFBamBuffIndexByIdxTableIdxKey)schema.getFactoryIndex().newByIdxTableIdxKey();
		key.setRequiredTableId( TableId );

		ICFBamIndex[] recArray;
		if( dictByIdxTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTableIdx
				= dictByIdxTableIdx.get( key );
			recArray = new ICFBamIndex[ subdictIdxTableIdx.size() ];
			Iterator< ICFBamIndex > iter = subdictIdxTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTableIdx.put( key, subdictIdxTableIdx );
			recArray = new ICFBamIndex[0];
		}
		return( recArray );
	}

	public ICFBamIndex[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByDefSchemaIdx";
		CFBamBuffIndexByDefSchemaIdxKey key = (CFBamBuffIndexByDefSchemaIdxKey)schema.getFactoryIndex().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamIndex[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamIndex[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamIndex > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamIndex[0];
		}
		return( recArray );
	}

	public ICFBamIndex readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamIndex buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndex readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readBuff";
		ICFBamIndex buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndex.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndex lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamIndex buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndex.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndex[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndex.readAllBuff";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	public ICFBamIndex readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamIndex buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamIndex)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamIndex[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	public ICFBamIndex readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByUNameIdx() ";
		ICFBamIndex buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
			return( (ICFBamIndex)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamIndex[] readBuffByIdxTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByIdxTableIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByIdxTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	public ICFBamIndex[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByDefSchemaIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
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
	public ICFBamIndex[] pageBuffByIdxTableIdx( ICFSecAuthorization Authorization,
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
	public ICFBamIndex[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamIndex updateIndex( ICFSecAuthorization Authorization,
		ICFBamIndex Buff )
	{
		ICFBamIndex repl = schema.getTableScope().updateScope( Authorization,
			Buff );
		if (repl != Buff) {
			throw new CFLibInvalidStateException(getClass(), S_ProcName, "repl != Buff", "repl != Buff");
		}
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamIndex existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndex",
				"Existing record not found",
				"Index",
				pkey );
		}
		CFBamBuffIndexByUNameIdxKey existingKeyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getFactoryIndex().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexByUNameIdxKey newKeyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getFactoryIndex().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexByIdxTableIdxKey existingKeyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getFactoryIndex().newByIdxTableIdxKey();
		existingKeyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffIndexByIdxTableIdxKey newKeyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getFactoryIndex().newByIdxTableIdxKey();
		newKeyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffIndexByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getFactoryIndex().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffIndexByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getFactoryIndex().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndex",
					"IndexUNameIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteIndex( ICFSecAuthorization Authorization,
		ICFBamIndex Buff )
	{
		final String S_ProcName = "CFBamRamIndexTable.deleteIndex() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamIndex existing = dictByPKey.get( pkey );
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
		CFBamBuffIndexByUNameIdxKey keyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getFactoryIndex().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexByIdxTableIdxKey keyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getFactoryIndex().newByIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffIndexByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getFactoryIndex().newByDefSchemaIdxKey();
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
		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIdxTableIdx.get( keyIdxTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteIndexByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffIndexByUNameIdxKey key = (CFBamBuffIndexByUNameIdxKey)schema.getFactoryIndex().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteIndexByUNameIdx( Authorization, key );
	}

	public void deleteIndexByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByUNameIdxKey argKey )
	{
		ICFBamIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndex> matchSet = new LinkedList<ICFBamIndex>();
		Iterator<ICFBamIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIdxTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffIndexByIdxTableIdxKey key = (CFBamBuffIndexByIdxTableIdxKey)schema.getFactoryIndex().newByIdxTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteIndexByIdxTableIdx( Authorization, key );
	}

	public void deleteIndexByIdxTableIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByIdxTableIdxKey argKey )
	{
		ICFBamIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndex> matchSet = new LinkedList<ICFBamIndex>();
		Iterator<ICFBamIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffIndexByDefSchemaIdxKey key = (CFBamBuffIndexByDefSchemaIdxKey)schema.getFactoryIndex().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexByDefSchemaIdx( Authorization, key );
	}

	public void deleteIndexByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByDefSchemaIdxKey argKey )
	{
		ICFBamIndex cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndex> matchSet = new LinkedList<ICFBamIndex>();
		Iterator<ICFBamIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamIndex cur;
		LinkedList<ICFBamIndex> matchSet = new LinkedList<ICFBamIndex>();
		Iterator<ICFBamIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexByTenantIdx( Authorization, key );
	}

	public void deleteIndexByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndex> matchSet = new LinkedList<ICFBamIndex>();
		Iterator<ICFBamIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}
}
