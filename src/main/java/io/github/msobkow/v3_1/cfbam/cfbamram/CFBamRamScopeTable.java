
// Description: Java 25 in-memory RAM DbIO implementation for Scope.

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
 *	CFBamRamScopeTable in-memory RAM DbIO implementation
 *	for Scope.
 */
public class CFBamRamScopeTable
	implements ICFBamScopeTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffScope > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffScope >();
	private Map< CFBamBuffScopeByTenantIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffScope >> dictByTenantIdx
		= new HashMap< CFBamBuffScopeByTenantIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffScope >>();

	public CFBamRamScopeTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createScope( ICFSecAuthorization Authorization,
		ICFBamScope Buff )
	{
		final String S_ProcName = "createScope";
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( schema.nextScopeIdGen() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamBuffScopeByTenantIdxKey keyTenantIdx = schema.getFactoryScope().newTenantIdxKey();
		keyTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx;
		if( dictByTenantIdx.containsKey( keyTenantIdx ) ) {
			subdictTenantIdx = dictByTenantIdx.get( keyTenantIdx );
		}
		else {
			subdictTenantIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffScope >();
			dictByTenantIdx.put( keyTenantIdx, subdictTenantIdx );
		}
		subdictTenantIdx.put( pkey, Buff );

	}

	public ICFBamScope readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamScope.readDerived";
		ICFBamScope buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamScope.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamScope buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamScope.readAllDerived";
		ICFBamScope[] retList = new ICFBamScope[ dictByPKey.values().size() ];
		Iterator< ICFBamScope > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamScope[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( TenantId );

		ICFBamScope[] recArray;
		if( dictByTenantIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx
				= dictByTenantIdx.get( key );
			recArray = new ICFBamScope[ subdictTenantIdx.size() ];
			Iterator< ICFBamScope > iter = subdictTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffScope >();
			dictByTenantIdx.put( key, subdictTenantIdx );
			recArray = new ICFBamScope[0];
		}
		return( recArray );
	}

	public ICFBamScope readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		ICFBamScope buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamScope.readBuff";
		ICFBamScope buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a801" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamScope buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a801" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamScope.readAllBuff";
		ICFBamScope buff;
		ArrayList<ICFBamScope> filteredList = new ArrayList<ICFBamScope>();
		ICFBamScope[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamScope[0] ) );
	}

	public ICFBamScope readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamScope buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (ICFBamScope)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamScope[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamScope buff;
		ArrayList<ICFBamScope> filteredList = new ArrayList<ICFBamScope>();
		ICFBamScope[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (ICFBamScope)buff );
			}
		}
		return( filteredList.toArray( new ICFBamScope[0] ) );
	}

	public void updateScope( ICFSecAuthorization Authorization,
		ICFBamScope Buff )
	{
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamScope existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateScope",
				"Existing record not found",
				"Scope",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateScope",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffScopeByTenantIdxKey existingKeyTenantIdx = schema.getFactoryScope().newTenantIdxKey();
		existingKeyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamBuffScopeByTenantIdxKey newKeyTenantIdx = schema.getFactoryScope().newTenantIdxKey();
		newKeyTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateScope",
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffScope > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTenantIdx.get( existingKeyTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTenantIdx.containsKey( newKeyTenantIdx ) ) {
			subdict = dictByTenantIdx.get( newKeyTenantIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffScope >();
			dictByTenantIdx.put( newKeyTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteScope( ICFSecAuthorization Authorization,
		ICFBamScope Buff )
	{
		final String S_ProcName = "CFBamRamScopeTable.deleteScope() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamScope existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteScope",
				pkey );
		}
		CFBamBuffScopeByTenantIdxKey keyTenantIdx = schema.getFactoryScope().newTenantIdxKey();
		keyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		// Validate reverse foreign keys

		if( schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"SchemaDef",
				pkey );
		}

		if( schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"SchemaRef",
				pkey );
		}

		if( schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"ServerMethod",
				pkey );
		}

		if( schema.getTableTable().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"Table",
				pkey );
		}

		if( schema.getTableClearDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"ClearDep",
				pkey );
		}

		if( schema.getTableDelDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"DelDep",
				pkey );
		}

		if( schema.getTableIndex().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"Index",
				pkey );
		}

		if( schema.getTablePopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"PopDep",
				pkey );
		}

		if( schema.getTableRelation().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"SuperClass",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffScope > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTenantIdx.get( keyTenantIdx );
		subdict.remove( pkey );

	}
	public void deleteScopeByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteScopeByIdIdx( Authorization, key );
	}

	public void deleteScopeByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteScopeByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamScope cur;
		LinkedList<ICFBamScope> matchSet = new LinkedList<ICFBamScope>();
		Iterator<ICFBamScope> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamScope> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableScope().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a801".equals( subClassCode ) ) {
				schema.getTableScope().deleteScope( Authorization, cur );
			}
			else if( "a802".equals( subClassCode ) ) {
				schema.getTableSchemaDef().deleteSchemaDef( Authorization, (ICFBamSchemaDef)cur );
			}
			else if( "a804".equals( subClassCode ) ) {
				schema.getTableSchemaRef().deleteSchemaRef( Authorization, (ICFBamSchemaRef)cur );
			}
			else if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, (ICFBamServerMethod)cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else if( "a808".equals( subClassCode ) ) {
				schema.getTableTable().deleteTable( Authorization, (ICFBamTable)cur );
			}
			else if( "a810".equals( subClassCode ) ) {
				schema.getTableClearDep().deleteClearDep( Authorization, (ICFBamClearDep)cur );
			}
			else if( "a811".equals( subClassCode ) ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( "a812".equals( subClassCode ) ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( "a813".equals( subClassCode ) ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( "a814".equals( subClassCode ) ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, (ICFBamDelDep)cur );
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
			else if( "a821".equals( subClassCode ) ) {
				schema.getTableIndex().deleteIndex( Authorization, (ICFBamIndex)cur );
			}
			else if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, (ICFBamPopDep)cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else if( "a835".equals( subClassCode ) ) {
				schema.getTableRelation().deleteRelation( Authorization, (ICFBamRelation)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Scope must not be \"" + subClassCode + "\"" );
			}
		}
	}

	public void deleteScopeByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteScopeByTenantIdx( Authorization, key );
	}

	public void deleteScopeByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteScopeByTenantIdx";
		ICFBamScope cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamScope> matchSet = new LinkedList<ICFBamScope>();
		Iterator<ICFBamScope> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamScope> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableScope().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a801".equals( subClassCode ) ) {
				schema.getTableScope().deleteScope( Authorization, cur );
			}
			else if( "a802".equals( subClassCode ) ) {
				schema.getTableSchemaDef().deleteSchemaDef( Authorization, (ICFBamSchemaDef)cur );
			}
			else if( "a804".equals( subClassCode ) ) {
				schema.getTableSchemaRef().deleteSchemaRef( Authorization, (ICFBamSchemaRef)cur );
			}
			else if( "a805".equals( subClassCode ) ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, (ICFBamServerMethod)cur );
			}
			else if( "a806".equals( subClassCode ) ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( "a807".equals( subClassCode ) ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( "a837".equals( subClassCode ) ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else if( "a808".equals( subClassCode ) ) {
				schema.getTableTable().deleteTable( Authorization, (ICFBamTable)cur );
			}
			else if( "a810".equals( subClassCode ) ) {
				schema.getTableClearDep().deleteClearDep( Authorization, (ICFBamClearDep)cur );
			}
			else if( "a811".equals( subClassCode ) ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( "a812".equals( subClassCode ) ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( "a813".equals( subClassCode ) ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( "a814".equals( subClassCode ) ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else if( "a817".equals( subClassCode ) ) {
				schema.getTableDelDep().deleteDelDep( Authorization, (ICFBamDelDep)cur );
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
			else if( "a821".equals( subClassCode ) ) {
				schema.getTableIndex().deleteIndex( Authorization, (ICFBamIndex)cur );
			}
			else if( "a830".equals( subClassCode ) ) {
				schema.getTablePopDep().deletePopDep( Authorization, (ICFBamPopDep)cur );
			}
			else if( "a831".equals( subClassCode ) ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( "a832".equals( subClassCode ) ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( "a833".equals( subClassCode ) ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( "a834".equals( subClassCode ) ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else if( "a835".equals( subClassCode ) ) {
				schema.getTableRelation().deleteRelation( Authorization, (ICFBamRelation)cur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"subClassCode",
					cur,
					"Instance of or subclass of Scope must not be \"" + subClassCode + "\"" );
			}
		}
	}
}
