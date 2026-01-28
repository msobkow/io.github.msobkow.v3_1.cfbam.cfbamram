
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

	public CFBamBuffScope ensureRec(ICFBamScope rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamScope.CLASS_CODE) {
				return( ((CFBamBuffScopeDefaultFactory)(schema.getFactoryScope())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$SchemaDef.CLASS_CODE) {
				return( ((CFBamBuffSchemaDefDefaultFactory)(schema.getFactorySchemaDef())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$SchemaRef.CLASS_CODE) {
				return( ((CFBamBuffSchemaRefDefaultFactory)(schema.getFactorySchemaRef())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ServerMethod.CLASS_CODE) {
				return( ((CFBamBuffServerMethodDefaultFactory)(schema.getFactoryServerMethod())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ServerObjFunc.CLASS_CODE) {
				return( ((CFBamBuffServerObjFuncDefaultFactory)(schema.getFactoryServerObjFunc())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ServerProc.CLASS_CODE) {
				return( ((CFBamBuffServerProcDefaultFactory)(schema.getFactoryServerProc())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ServerListFunc.CLASS_CODE) {
				return( ((CFBamBuffServerListFuncDefaultFactory)(schema.getFactoryServerListFunc())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$Table.CLASS_CODE) {
				return( ((CFBamBuffTableDefaultFactory)(schema.getFactoryTable())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ClearDep.CLASS_CODE) {
				return( ((CFBamBuffClearDepDefaultFactory)(schema.getFactoryClearDep())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ClearSubDep1.CLASS_CODE) {
				return( ((CFBamBuffClearSubDep1DefaultFactory)(schema.getFactoryClearSubDep1())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ClearSubDep2.CLASS_CODE) {
				return( ((CFBamBuffClearSubDep2DefaultFactory)(schema.getFactoryClearSubDep2())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ClearSubDep3.CLASS_CODE) {
				return( ((CFBamBuffClearSubDep3DefaultFactory)(schema.getFactoryClearSubDep3())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$ClearTopDep.CLASS_CODE) {
				return( ((CFBamBuffClearTopDepDefaultFactory)(schema.getFactoryClearTopDep())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$DelDep.CLASS_CODE) {
				return( ((CFBamBuffDelDepDefaultFactory)(schema.getFactoryDelDep())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$DelSubDep1.CLASS_CODE) {
				return( ((CFBamBuffDelSubDep1DefaultFactory)(schema.getFactoryDelSubDep1())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$DelSubDep2.CLASS_CODE) {
				return( ((CFBamBuffDelSubDep2DefaultFactory)(schema.getFactoryDelSubDep2())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$DelSubDep3.CLASS_CODE) {
				return( ((CFBamBuffDelSubDep3DefaultFactory)(schema.getFactoryDelSubDep3())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$DelTopDep.CLASS_CODE) {
				return( ((CFBamBuffDelTopDepDefaultFactory)(schema.getFactoryDelTopDep())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$Index.CLASS_CODE) {
				return( ((CFBamBuffIndexDefaultFactory)(schema.getFactoryIndex())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$PopDep.CLASS_CODE) {
				return( ((CFBamBuffPopDepDefaultFactory)(schema.getFactoryPopDep())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$PopSubDep1.CLASS_CODE) {
				return( ((CFBamBuffPopSubDep1DefaultFactory)(schema.getFactoryPopSubDep1())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$PopSubDep2.CLASS_CODE) {
				return( ((CFBamBuffPopSubDep2DefaultFactory)(schema.getFactoryPopSubDep2())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$PopSubDep3.CLASS_CODE) {
				return( ((CFBamBuffPopSubDep3DefaultFactory)(schema.getFactoryPopSubDep3())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$PopTopDep.CLASS_CODE) {
				return( ((CFBamBuffPopTopDepDefaultFactory)(schema.getFactoryPopTopDep())).ensureRec(rec) );
			}
			else if (classCode == I$DeffSchemaName$Relation.CLASS_CODE) {
				return( ((CFBamBuffRelationDefaultFactory)(schema.getFactoryRelation())).ensureRec(rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamScope createScope( ICFSecAuthorization Authorization,
		ICFBamScope iBuff )
	{
		final String S_ProcName = "createScope";
		
		CFBamBuffScope Buff = ensureRec(iBuff);
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextScopeIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffScopeByTenantIdxKey keyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
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
						"Owner",
						"Tenant",
						"Tenant",
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

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamScope.CLASS_CODE) {
				CFBamBuffScope retbuff = ((CFBamBuffScope)(schema.getFactoryScope().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamSchemaDef.CLASS_CODE) {
				CFBamBuffSchemaDef retbuff = ((CFBamBuffSchemaDef)(schema.getFactorySchemaDef().newRec()));
				retbuff.set((ICFBamSchemaDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamSchemaRef.CLASS_CODE) {
				CFBamBuffSchemaRef retbuff = ((CFBamBuffSchemaRef)(schema.getFactorySchemaRef().newRec()));
				retbuff.set((ICFBamSchemaRef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerMethod.CLASS_CODE) {
				CFBamBuffServerMethod retbuff = ((CFBamBuffServerMethod)(schema.getFactoryServerMethod().newRec()));
				retbuff.set((ICFBamServerMethod)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerObjFunc.CLASS_CODE) {
				CFBamBuffServerObjFunc retbuff = ((CFBamBuffServerObjFunc)(schema.getFactoryServerObjFunc().newRec()));
				retbuff.set((ICFBamServerObjFunc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerProc.CLASS_CODE) {
				CFBamBuffServerProc retbuff = ((CFBamBuffServerProc)(schema.getFactoryServerProc().newRec()));
				retbuff.set((ICFBamServerProc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerListFunc.CLASS_CODE) {
				CFBamBuffServerListFunc retbuff = ((CFBamBuffServerListFunc)(schema.getFactoryServerListFunc().newRec()));
				retbuff.set((ICFBamServerListFunc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTable.CLASS_CODE) {
				CFBamBuffTable retbuff = ((CFBamBuffTable)(schema.getFactoryTable().newRec()));
				retbuff.set((ICFBamTable)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearDep.CLASS_CODE) {
				CFBamBuffClearDep retbuff = ((CFBamBuffClearDep)(schema.getFactoryClearDep().newRec()));
				retbuff.set((ICFBamClearDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep1.CLASS_CODE) {
				CFBamBuffClearSubDep1 retbuff = ((CFBamBuffClearSubDep1)(schema.getFactoryClearSubDep1().newRec()));
				retbuff.set((ICFBamClearSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep2.CLASS_CODE) {
				CFBamBuffClearSubDep2 retbuff = ((CFBamBuffClearSubDep2)(schema.getFactoryClearSubDep2().newRec()));
				retbuff.set((ICFBamClearSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep3.CLASS_CODE) {
				CFBamBuffClearSubDep3 retbuff = ((CFBamBuffClearSubDep3)(schema.getFactoryClearSubDep3().newRec()));
				retbuff.set((ICFBamClearSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearTopDep.CLASS_CODE) {
				CFBamBuffClearTopDep retbuff = ((CFBamBuffClearTopDep)(schema.getFactoryClearTopDep().newRec()));
				retbuff.set((ICFBamClearTopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelDep.CLASS_CODE) {
				CFBamBuffDelDep retbuff = ((CFBamBuffDelDep)(schema.getFactoryDelDep().newRec()));
				retbuff.set((ICFBamDelDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep1.CLASS_CODE) {
				CFBamBuffDelSubDep1 retbuff = ((CFBamBuffDelSubDep1)(schema.getFactoryDelSubDep1().newRec()));
				retbuff.set((ICFBamDelSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep2.CLASS_CODE) {
				CFBamBuffDelSubDep2 retbuff = ((CFBamBuffDelSubDep2)(schema.getFactoryDelSubDep2().newRec()));
				retbuff.set((ICFBamDelSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep3.CLASS_CODE) {
				CFBamBuffDelSubDep3 retbuff = ((CFBamBuffDelSubDep3)(schema.getFactoryDelSubDep3().newRec()));
				retbuff.set((ICFBamDelSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelTopDep.CLASS_CODE) {
				CFBamBuffDelTopDep retbuff = ((CFBamBuffDelTopDep)(schema.getFactoryDelTopDep().newRec()));
				retbuff.set((ICFBamDelTopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamIndex.CLASS_CODE) {
				CFBamBuffIndex retbuff = ((CFBamBuffIndex)(schema.getFactoryIndex().newRec()));
				retbuff.set((ICFBamIndex)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopDep.CLASS_CODE) {
				CFBamBuffPopDep retbuff = ((CFBamBuffPopDep)(schema.getFactoryPopDep().newRec()));
				retbuff.set((ICFBamPopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep1.CLASS_CODE) {
				CFBamBuffPopSubDep1 retbuff = ((CFBamBuffPopSubDep1)(schema.getFactoryPopSubDep1().newRec()));
				retbuff.set((ICFBamPopSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep2.CLASS_CODE) {
				CFBamBuffPopSubDep2 retbuff = ((CFBamBuffPopSubDep2)(schema.getFactoryPopSubDep2().newRec()));
				retbuff.set((ICFBamPopSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep3.CLASS_CODE) {
				CFBamBuffPopSubDep3 retbuff = ((CFBamBuffPopSubDep3)(schema.getFactoryPopSubDep3().newRec()));
				retbuff.set((ICFBamPopSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopTopDep.CLASS_CODE) {
				CFBamBuffPopTopDep retbuff = ((CFBamBuffPopTopDep)(schema.getFactoryPopTopDep().newRec()));
				retbuff.set((ICFBamPopTopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamRelation.CLASS_CODE) {
				CFBamBuffRelation retbuff = ((CFBamBuffRelation)(schema.getFactoryRelation().newRec()));
				retbuff.set((ICFBamRelation)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
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
		ICFBamScope buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamScope.readAllDerived";
		ICFBamScope[] retList = new ICFBamScope[ dictByPKey.values().size() ];
		Iterator< CFBamBuffScope > iter = dictByPKey.values().iterator();
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
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( TenantId );

		ICFBamScope[] recArray;
		if( dictByTenantIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx
				= dictByTenantIdx.get( key );
			recArray = new ICFBamScope[ subdictTenantIdx.size() ];
			Iterator< CFBamBuffScope > iter = subdictTenantIdx.values().iterator();
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
		ICFBamScope buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
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
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamScope.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamScope lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamScope buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamScope.CLASS_CODE ) ) {
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
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
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
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
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
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamScope)buff );
			}
		}
		return( filteredList.toArray( new ICFBamScope[0] ) );
	}

	public ICFBamScope updateScope( ICFSecAuthorization Authorization,
		ICFBamScope iBuff )
	{
		CFBamBuffScope Buff = ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffScope existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateScope",
				"Existing record not found",
				"Existing record not found",
				"Scope",
				"Scope",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateScope",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffScopeByTenantIdxKey existingKeyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		existingKeyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamBuffScopeByTenantIdxKey newKeyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
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
						"Owner",
						"Tenant",
						"Tenant",
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

		return(Buff);
	}

	public void deleteScope( ICFSecAuthorization Authorization,
		ICFBamScope iBuff )
	{
		final String S_ProcName = "CFBamRamScopeTable.deleteScope() ";
		CFBamBuffScope Buff = ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffScope existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteScope",
				pkey );
		}
		CFBamBuffScopeByTenantIdxKey keyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
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
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteScopeByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffScope cur;
		LinkedList<CFBamBuffScope> matchSet = new LinkedList<CFBamBuffScope>();
		Iterator<CFBamBuffScope> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffScope> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffScope)(schema.getTableScope().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( CFBamScope.CLASS_CODE == subClassCode ) {
				schema.getTableScope().deleteScope( Authorization, cur );
			}
			else if( CFBamSchemaDef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaDef().deleteSchemaDef( Authorization, (ICFBamSchemaDef)cur );
			}
			else if( CFBamSchemaRef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRef().deleteSchemaRef( Authorization, (ICFBamSchemaRef)cur );
			}
			else if( CFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, (ICFBamServerMethod)cur );
			}
			else if( CFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( CFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( CFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else if( CFBamTable.CLASS_CODE == subClassCode ) {
				schema.getTableTable().deleteTable( Authorization, (ICFBamTable)cur );
			}
			else if( CFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, (ICFBamClearDep)cur );
			}
			else if( CFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( CFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( CFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( CFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else if( CFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, (ICFBamDelDep)cur );
			}
			else if( CFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( CFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( CFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( CFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else if( CFBamIndex.CLASS_CODE == subClassCode ) {
				schema.getTableIndex().deleteIndex( Authorization, (ICFBamIndex)cur );
			}
			else if( CFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, (ICFBamPopDep)cur );
			}
			else if( CFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( CFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( CFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( CFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else if( CFBamRelation.CLASS_CODE == subClassCode ) {
				schema.getTableRelation().deleteRelation( Authorization, (ICFBamRelation)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	public void deleteScopeByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteScopeByTenantIdx( Authorization, key );
	}

	public void deleteScopeByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteScopeByTenantIdx";
		CFBamBuffScope cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffScope> matchSet = new LinkedList<CFBamBuffScope>();
		Iterator<CFBamBuffScope> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffScope> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffScope)(schema.getTableScope().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( CFBamScope.CLASS_CODE == subClassCode ) {
				schema.getTableScope().deleteScope( Authorization, cur );
			}
			else if( CFBamSchemaDef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaDef().deleteSchemaDef( Authorization, (ICFBamSchemaDef)cur );
			}
			else if( CFBamSchemaRef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRef().deleteSchemaRef( Authorization, (ICFBamSchemaRef)cur );
			}
			else if( CFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, (ICFBamServerMethod)cur );
			}
			else if( CFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( CFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( CFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else if( CFBamTable.CLASS_CODE == subClassCode ) {
				schema.getTableTable().deleteTable( Authorization, (ICFBamTable)cur );
			}
			else if( CFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, (ICFBamClearDep)cur );
			}
			else if( CFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( CFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( CFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( CFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else if( CFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, (ICFBamDelDep)cur );
			}
			else if( CFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( CFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( CFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( CFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else if( CFBamIndex.CLASS_CODE == subClassCode ) {
				schema.getTableIndex().deleteIndex( Authorization, (ICFBamIndex)cur );
			}
			else if( CFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, (ICFBamPopDep)cur );
			}
			else if( CFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( CFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( CFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( CFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else if( CFBamRelation.CLASS_CODE == subClassCode ) {
				schema.getTableRelation().deleteRelation( Authorization, (ICFBamRelation)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}
}
