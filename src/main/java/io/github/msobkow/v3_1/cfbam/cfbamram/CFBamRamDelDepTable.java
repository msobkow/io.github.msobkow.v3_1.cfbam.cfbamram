
// Description: Java 25 in-memory RAM DbIO implementation for DelDep.

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
 *	CFBamRamDelDepTable in-memory RAM DbIO implementation
 *	for DelDep.
 */
public class CFBamRamDelDepTable
	implements ICFBamDelDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelDep >();
	private Map< CFBamBuffDelDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffDelDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >>();
	private Map< CFBamBuffDelDepByDelDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >> dictByDelDepIdx
		= new HashMap< CFBamBuffDelDepByDelDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >>();

	public CFBamRamDelDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createDelDep( ICFSecAuthorization Authorization,
		ICFBamDelDep Buff )
	{
		final String S_ProcName = "createDelDep";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamBuffDelDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDelDepIdxKey keyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		keyDelDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDelDepIdx;
		if( dictByDelDepIdx.containsKey( keyDelDepIdx ) ) {
			subdictDelDepIdx = dictByDelDepIdx.get( keyDelDepIdx );
		}
		else {
			subdictDelDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDelDepIdx.put( keyDelDepIdx, subdictDelDepIdx );
		}
		subdictDelDepIdx.put( pkey, Buff );

	}

	public ICFBamDelDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerived";
		ICFBamDelDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamDelDep buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelDep.readAllDerived";
		ICFBamDelDep[] retList = new ICFBamDelDep[ dictByPKey.values().size() ];
		Iterator< ICFBamDelDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamDelDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelDep ) ) {
					filteredList.add( (ICFBamDelDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelDep[0] ) );
		}
	}

	public ICFBamDelDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		CFBamBuffDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamDelDep[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamDelDep[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamDelDep > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamDelDep[0];
		}
		return( recArray );
	}

	public ICFBamDelDep[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		CFBamBuffDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationId( RelationId );

		ICFBamDelDep[] recArray;
		if( dictByDelDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDelDepIdx
				= dictByDelDepIdx.get( key );
			recArray = new ICFBamDelDep[ subdictDelDepIdx.size() ];
			Iterator< ICFBamDelDep > iter = subdictDelDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDelDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDelDepIdx.put( key, subdictDelDepIdx );
			recArray = new ICFBamDelDep[0];
		}
		return( recArray );
	}

	public ICFBamDelDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		ICFBamDelDep buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelDep readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuff";
		ICFBamDelDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a817" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelDep lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamDelDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a817" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelDep[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelDep.readAllBuff";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	public ICFBamDelDep readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamDelDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (ICFBamDelDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelDep[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (ICFBamDelDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	public ICFBamDelDep[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (ICFBamDelDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	public ICFBamDelDep[] readBuffByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (ICFBamDelDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	/**
	 *	Read a page array of the specific DelDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The DelDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamDelDep[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelDep buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The DelDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamDelDep[] pageBuffByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateDelDep( ICFSecAuthorization Authorization,
		ICFBamDelDep Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamDelDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelDep",
				"Existing record not found",
				"DelDep",
				pkey );
		}
		CFBamBuffDelDepByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDelDepIdxKey existingKeyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		existingKeyDelDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffDelDepByDelDepIdxKey newKeyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		newKeyDelDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelDep",
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelDep",
						"Lookup",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDelDepIdx.get( existingKeyDelDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelDepIdx.containsKey( newKeyDelDepIdx ) ) {
			subdict = dictByDelDepIdx.get( newKeyDelDepIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDelDepIdx.put( newKeyDelDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteDelDep( ICFSecAuthorization Authorization,
		ICFBamDelDep Buff )
	{
		final String S_ProcName = "CFBamRamDelDepTable.deleteDelDep() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamDelDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelDep",
				pkey );
		}
		CFBamBuffDelDepByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryDelDep().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDelDepIdxKey keyDelDepIdx = schema.getFactoryDelDep().newDelDepIdxKey();
		keyDelDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		// Validate reverse foreign keys

		if( schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelSubDep1",
				pkey );
		}

		if( schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelSubDep2",
				pkey );
		}

		if( schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelSubDep3",
				pkey );
		}

		if( schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"SuperClass",
				"DelTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByDelDepIdx.get( keyDelDepIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteDelDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByDefSchemaIdx";
		ICFBamDelDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamDelDep> matchSet = new LinkedList<ICFBamDelDep>();
		Iterator<ICFBamDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteDelDepByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelDepByDelDepIdx( Authorization, key );
	}

	public void deleteDelDepByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByDelDepIdx";
		ICFBamDelDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamDelDep> matchSet = new LinkedList<ICFBamDelDep>();
		Iterator<ICFBamDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteDelDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteDelDepByIdIdx( Authorization, key );
	}

	public void deleteDelDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteDelDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamDelDep cur;
		LinkedList<ICFBamDelDep> matchSet = new LinkedList<ICFBamDelDep>();
		Iterator<ICFBamDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteDelDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelDepByTenantIdx( Authorization, key );
	}

	public void deleteDelDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByTenantIdx";
		ICFBamDelDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamDelDep> matchSet = new LinkedList<ICFBamDelDep>();
		Iterator<ICFBamDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( "a818".equals( subClassCode ) ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( "a819".equals( subClassCode ) ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( "a81a".equals( subClassCode ) ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( "a81b".equals( subClassCode ) ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of DelDep must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
