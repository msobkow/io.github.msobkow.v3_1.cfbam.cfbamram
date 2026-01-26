
// Description: Java 25 in-memory RAM DbIO implementation for Tenant.

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
 *	CFBamRamTenantTable in-memory RAM DbIO implementation
 *	for Tenant.
 */
public class CFBamRamTenantTable
	implements ICFBamTenantTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFSecBuffTenant > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFSecBuffTenant >();
	private Map< CFSecBuffTenantByClusterIdxKey,
				Map< CFLibDbKeyHash256,
					CFSecBuffTenant >> dictByClusterIdx
		= new HashMap< CFSecBuffTenantByClusterIdxKey,
				Map< CFLibDbKeyHash256,
					CFSecBuffTenant >>();
	private Map< CFSecBuffTenantByUNameIdxKey,
			CFSecBuffTenant > dictByUNameIdx
		= new HashMap< CFSecBuffTenantByUNameIdxKey,
			CFSecBuffTenant >();

	public CFBamRamTenantTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFSecTenant createTenant( ICFSecAuthorization Authorization,
		ICFSecTenant Buff )
	{
		final String S_ProcName = "createTenant";
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextTenantIdGen();
		Buff.setRequiredId( pkey );
		CFSecBuffTenantByClusterIdxKey keyClusterIdx = (CFSecBuffTenantByClusterIdxKey)schema.getFactoryTenant().newByClusterIdxKey();
		keyClusterIdx.setRequiredClusterId( Buff.getRequiredClusterId() );

		CFSecBuffTenantByUNameIdxKey keyUNameIdx = (CFSecBuffTenantByUNameIdxKey)schema.getFactoryTenant().newByUNameIdxKey();
		keyUNameIdx.setRequiredClusterId( Buff.getRequiredClusterId() );
		keyUNameIdx.setRequiredTenantName( Buff.getRequiredTenantName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TenantUNameIdx",
				"TenantUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableCluster().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClusterId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"TenantCluster",
						"Cluster",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFSecBuffTenant > subdictClusterIdx;
		if( dictByClusterIdx.containsKey( keyClusterIdx ) ) {
			subdictClusterIdx = dictByClusterIdx.get( keyClusterIdx );
		}
		else {
			subdictClusterIdx = new HashMap< CFLibDbKeyHash256, CFSecBuffTenant >();
			dictByClusterIdx.put( keyClusterIdx, subdictClusterIdx );
		}
		subdictClusterIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		return( Buff );
	}

	public ICFSecTenant readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTenant.readDerived";
		ICFSecTenant buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFSecTenant lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTenant.readDerived";
		ICFSecTenant buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFSecTenant[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTenant.readAllDerived";
		ICFSecTenant[] retList = new ICFSecTenant[ dictByPKey.values().size() ];
		Iterator< ICFSecTenant > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFSecTenant[] readDerivedByClusterIdx( ICFSecAuthorization Authorization,
		long ClusterId )
	{
		final String S_ProcName = "CFBamRamTenant.readDerivedByClusterIdx";
		CFSecBuffTenantByClusterIdxKey key = (CFSecBuffTenantByClusterIdxKey)schema.getFactoryTenant().newByClusterIdxKey();
		key.setRequiredClusterId( ClusterId );

		ICFSecTenant[] recArray;
		if( dictByClusterIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFSecBuffTenant > subdictClusterIdx
				= dictByClusterIdx.get( key );
			recArray = new ICFSecTenant[ subdictClusterIdx.size() ];
			Iterator< ICFSecTenant > iter = subdictClusterIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFSecBuffTenant > subdictClusterIdx
				= new HashMap< CFLibDbKeyHash256, CFSecBuffTenant >();
			dictByClusterIdx.put( key, subdictClusterIdx );
			recArray = new ICFSecTenant[0];
		}
		return( recArray );
	}

	public ICFSecTenant readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		long ClusterId,
		String TenantName )
	{
		final String S_ProcName = "CFBamRamTenant.readDerivedByUNameIdx";
		CFSecBuffTenantByUNameIdxKey key = (CFSecBuffTenantByUNameIdxKey)schema.getFactoryTenant().newByUNameIdxKey();
		key.setRequiredClusterId( ClusterId );
		key.setRequiredTenantName( TenantName );

		ICFSecTenant buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFSecTenant readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTenant.readDerivedByIdIdx() ";
		ICFSecTenant buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFSecTenant readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTenant.readBuff";
		ICFSecTenant buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFSecTenant.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFSecTenant lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFSecTenant buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFSecTenant.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFSecTenant[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTenant.readAllBuff";
		ICFSecTenant buff;
		ArrayList<ICFSecTenant> filteredList = new ArrayList<ICFSecTenant>();
		ICFSecTenant[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFSecTenant.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFSecTenant[0] ) );
	}

	/**
	 *	Read a page of all the specific Tenant buffer instances.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@return All the specific Tenant instances in the database accessible for the Authorization.
	 */
	public ICFSecTenant[] pageAllBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageAllBuff";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFSecTenant readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTenant.readBuffByIdIdx() ";
		ICFSecTenant buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFSecTenant.CLASS_CODE ) ) {
			return( (ICFSecTenant)buff );
		}
		else {
			return( null );
		}
	}

	public ICFSecTenant[] readBuffByClusterIdx( ICFSecAuthorization Authorization,
		long ClusterId )
	{
		final String S_ProcName = "CFBamRamTenant.readBuffByClusterIdx() ";
		ICFSecTenant buff;
		ArrayList<ICFSecTenant> filteredList = new ArrayList<ICFSecTenant>();
		ICFSecTenant[] buffList = readDerivedByClusterIdx( Authorization,
			ClusterId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFSecTenant.CLASS_CODE ) ) {
				filteredList.add( (ICFSecTenant)buff );
			}
		}
		return( filteredList.toArray( new ICFSecTenant[0] ) );
	}

	public ICFSecTenant readBuffByUNameIdx( ICFSecAuthorization Authorization,
		long ClusterId,
		String TenantName )
	{
		final String S_ProcName = "CFBamRamTenant.readBuffByUNameIdx() ";
		ICFSecTenant buff = readDerivedByUNameIdx( Authorization,
			ClusterId,
			TenantName );
		if( ( buff != null ) && ( buff.getClassCode() == ICFSecTenant.CLASS_CODE ) ) {
			return( (ICFSecTenant)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific Tenant buffer instances identified by the duplicate key ClusterIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ClusterId	The Tenant key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFSecTenant[] pageBuffByClusterIdx( ICFSecAuthorization Authorization,
		long ClusterId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClusterIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFSecTenant updateTenant( ICFSecAuthorization Authorization,
		ICFSecTenant Buff )
	{
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFSecTenant existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTenant",
				"Existing record not found",
				"Tenant",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateTenant",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFSecBuffTenantByClusterIdxKey existingKeyClusterIdx = (CFSecBuffTenantByClusterIdxKey)schema.getFactoryTenant().newByClusterIdxKey();
		existingKeyClusterIdx.setRequiredClusterId( existing.getRequiredClusterId() );

		CFSecBuffTenantByClusterIdxKey newKeyClusterIdx = (CFSecBuffTenantByClusterIdxKey)schema.getFactoryTenant().newByClusterIdxKey();
		newKeyClusterIdx.setRequiredClusterId( Buff.getRequiredClusterId() );

		CFSecBuffTenantByUNameIdxKey existingKeyUNameIdx = (CFSecBuffTenantByUNameIdxKey)schema.getFactoryTenant().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredClusterId( existing.getRequiredClusterId() );
		existingKeyUNameIdx.setRequiredTenantName( existing.getRequiredTenantName() );

		CFSecBuffTenantByUNameIdxKey newKeyUNameIdx = (CFSecBuffTenantByUNameIdxKey)schema.getFactoryTenant().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredClusterId( Buff.getRequiredClusterId() );
		newKeyUNameIdx.setRequiredTenantName( Buff.getRequiredTenantName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTenant",
					"TenantUNameIdx",
					"TenantUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableCluster().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClusterId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTenant",
						"Container",
						"TenantCluster",
						"Cluster",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFSecBuffTenant > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClusterIdx.get( existingKeyClusterIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClusterIdx.containsKey( newKeyClusterIdx ) ) {
			subdict = dictByClusterIdx.get( newKeyClusterIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFSecBuffTenant >();
			dictByClusterIdx.put( newKeyClusterIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	public void deleteTenant( ICFSecAuthorization Authorization,
		ICFSecTenant Buff )
	{
		final String S_ProcName = "CFBamRamTenantTable.deleteTenant() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryTenant().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFSecTenant existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTenant",
				pkey );
		}
			CFBamSchemaDefBuff buffClearTableRelationNarrowed;
			CFBamSchemaDefBuff arrClearTableRelationNarrowed[] = schema.getTableSchemaDef().readDerivedByCTenantIdx( Authorization,
			existing.getRequiredId() );
			for( int idxClearTableRelationNarrowed = 0; idxClearTableRelationNarrowed < arrClearTableRelationNarrowed.length; idxClearTableRelationNarrowed++ ) {
				buffClearTableRelationNarrowed = arrClearTableRelationNarrowed[idxClearTableRelationNarrowed];
				CFBamTableBuff buffTables;
				CFBamTableBuff arrTables[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
				buffClearTableRelationNarrowed.getRequiredId() );
				for( int idxTables = 0; idxTables < arrTables.length; idxTables++ ) {
					buffTables = arrTables[idxTables];
					CFBamRelationBuff buffTableRelation;
					CFBamRelationBuff arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
						buffTables.getRequiredId() );
					for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
						buffTableRelation = arrTableRelation[idxTableRelation];
					{
						CFBamRelationBuff editBuff = schema.getTableRelation().readDerivedByIdIdx( Authorization,
							buffTables.getRequiredId() );
						editBuff.setOptionalNarrowedId( null );
						classCode = editBuff.getClassCode();
						if( classCode.equals( "a835" ) ) {
							schema.getTableRelation().updateRelation( Authorization, editBuff );
						}
						else {
							new CFLibUnsupportedClassException( getClass(),
								S_ProcName,
								"Unrecognized ClassCode \"" + classCode + "\"" );
						}
					}
				
					}
				}
			}
		CFBamSchemaDefBuff buffDelTableChain;
		CFBamSchemaDefBuff arrDelTableChain[] = schema.getTableSchemaDef().readDerivedByCTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableChain = 0; idxDelTableChain < arrDelTableChain.length; idxDelTableChain++ ) {
			buffDelTableChain = arrDelTableChain[idxDelTableChain];
			CFBamTableBuff buffTables;
			CFBamTableBuff arrTables[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
				buffDelTableChain.getRequiredId() );
			for( int idxTables = 0; idxTables < arrTables.length; idxTables++ ) {
				buffTables = arrTables[idxTables];
					schema.getTableChain().deleteChainByChainTableIdx( Authorization,
						buffTables.getRequiredId() );
			}
		}
		CFBamSchemaDefBuff buffDelTableRelationCol;
		CFBamSchemaDefBuff arrDelTableRelationCol[] = schema.getTableSchemaDef().readDerivedByCTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationCol = 0; idxDelTableRelationCol < arrDelTableRelationCol.length; idxDelTableRelationCol++ ) {
			buffDelTableRelationCol = arrDelTableRelationCol[idxDelTableRelationCol];
			CFBamTableBuff buffTables;
			CFBamTableBuff arrTables[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
				buffDelTableRelationCol.getRequiredId() );
			for( int idxTables = 0; idxTables < arrTables.length; idxTables++ ) {
				buffTables = arrTables[idxTables];
			CFBamRelationBuff buffTableRelation;
			CFBamRelationBuff arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
					buffTables.getRequiredId() );
			for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
				buffTableRelation = arrTableRelation[idxTableRelation];
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						buffTableRelation.getRequiredId() );
			}
			}
		}
		CFBamSchemaDefBuff buffDelTableRelation;
		CFBamSchemaDefBuff arrDelTableRelation[] = schema.getTableSchemaDef().readDerivedByCTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelation = 0; idxDelTableRelation < arrDelTableRelation.length; idxDelTableRelation++ ) {
			buffDelTableRelation = arrDelTableRelation[idxDelTableRelation];
			CFBamTableBuff buffTables;
			CFBamTableBuff arrTables[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
				buffDelTableRelation.getRequiredId() );
			for( int idxTables = 0; idxTables < arrTables.length; idxTables++ ) {
				buffTables = arrTables[idxTables];
					schema.getTableRelation().deleteRelationByRelTableIdx( Authorization,
						buffTables.getRequiredId() );
			}
		}
		CFBamSchemaDefBuff buffDelTable;
		CFBamSchemaDefBuff arrDelTable[] = schema.getTableSchemaDef().readDerivedByCTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTable = 0; idxDelTable < arrDelTable.length; idxDelTable++ ) {
			buffDelTable = arrDelTable[idxDelTable];
					schema.getTableTable().deleteTableBySchemaDefIdx( Authorization,
						buffDelTable.getRequiredId() );
		}
		CFBamSchemaDefBuff buffDelTypeDef;
		CFBamSchemaDefBuff arrDelTypeDef[] = schema.getTableSchemaDef().readDerivedByCTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTypeDef = 0; idxDelTypeDef < arrDelTypeDef.length; idxDelTypeDef++ ) {
			buffDelTypeDef = arrDelTypeDef[idxDelTypeDef];
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						buffDelTypeDef.getRequiredId() );
		}
					schema.getTableSchemaDef().deleteSchemaDefByCTenantIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableTld().deleteTldByTenantIdx( Authorization,
						existing.getRequiredId() );
		CFSecTSecGroupBuff buffDelIncludedByGroup;
		CFSecTSecGroupBuff arrDelIncludedByGroup[] = schema.getTableTSecGroup().readDerivedByTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelIncludedByGroup = 0; idxDelIncludedByGroup < arrDelIncludedByGroup.length; idxDelIncludedByGroup++ ) {
			buffDelIncludedByGroup = arrDelIncludedByGroup[idxDelIncludedByGroup];
					schema.getTableTSecGrpInc().deleteTSecGrpIncByIncludeIdx( Authorization,
						buffDelIncludedByGroup.getRequiredTSecGroupId() );
		}
		CFSecTSecGroupBuff buffDelGrpMembs;
		CFSecTSecGroupBuff arrDelGrpMembs[] = schema.getTableTSecGroup().readDerivedByTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelGrpMembs = 0; idxDelGrpMembs < arrDelGrpMembs.length; idxDelGrpMembs++ ) {
			buffDelGrpMembs = arrDelGrpMembs[idxDelGrpMembs];
					schema.getTableTSecGrpMemb().deleteTSecGrpMembByGroupIdx( Authorization,
						buffDelGrpMembs.getRequiredTSecGroupId() );
		}
		CFSecTSecGroupBuff buffDelGrpIncs;
		CFSecTSecGroupBuff arrDelGrpIncs[] = schema.getTableTSecGroup().readDerivedByTenantIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelGrpIncs = 0; idxDelGrpIncs < arrDelGrpIncs.length; idxDelGrpIncs++ ) {
			buffDelGrpIncs = arrDelGrpIncs[idxDelGrpIncs];
					schema.getTableTSecGrpInc().deleteTSecGrpIncByGroupIdx( Authorization,
						buffDelGrpIncs.getRequiredTSecGroupId() );
		}
					schema.getTableTSecGroup().deleteTSecGroupByTenantIdx( Authorization,
						existing.getRequiredId() );
		CFSecBuffTenantByClusterIdxKey keyClusterIdx = (CFSecBuffTenantByClusterIdxKey)schema.getFactoryTenant().newByClusterIdxKey();
		keyClusterIdx.setRequiredClusterId( existing.getRequiredClusterId() );

		CFSecBuffTenantByUNameIdxKey keyUNameIdx = (CFSecBuffTenantByUNameIdxKey)schema.getFactoryTenant().newByUNameIdxKey();
		keyUNameIdx.setRequiredClusterId( existing.getRequiredClusterId() );
		keyUNameIdx.setRequiredTenantName( existing.getRequiredTenantName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFSecBuffTenant > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClusterIdx.get( keyClusterIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

	}
	public void deleteTenantByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFSecTenant cur;
		LinkedList<ICFSecTenant> matchSet = new LinkedList<ICFSecTenant>();
		Iterator<ICFSecTenant> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFSecTenant> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTenant().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTenant( Authorization, cur );
		}
	}

	public void deleteTenantByClusterIdx( ICFSecAuthorization Authorization,
		long argClusterId )
	{
		CFSecBuffTenantByClusterIdxKey key = (CFSecBuffTenantByClusterIdxKey)schema.getFactoryTenant().newByClusterIdxKey();
		key.setRequiredClusterId( argClusterId );
		deleteTenantByClusterIdx( Authorization, key );
	}

	public void deleteTenantByClusterIdx( ICFSecAuthorization Authorization,
		ICFSecTenantByClusterIdxKey argKey )
	{
		ICFSecTenant cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFSecTenant> matchSet = new LinkedList<ICFSecTenant>();
		Iterator<ICFSecTenant> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFSecTenant> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTenant().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTenant( Authorization, cur );
		}
	}

	public void deleteTenantByUNameIdx( ICFSecAuthorization Authorization,
		long argClusterId,
		String argTenantName )
	{
		CFSecBuffTenantByUNameIdxKey key = (CFSecBuffTenantByUNameIdxKey)schema.getFactoryTenant().newByUNameIdxKey();
		key.setRequiredClusterId( argClusterId );
		key.setRequiredTenantName( argTenantName );
		deleteTenantByUNameIdx( Authorization, key );
	}

	public void deleteTenantByUNameIdx( ICFSecAuthorization Authorization,
		ICFSecTenantByUNameIdxKey argKey )
	{
		ICFSecTenant cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFSecTenant> matchSet = new LinkedList<ICFSecTenant>();
		Iterator<ICFSecTenant> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFSecTenant> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTenant().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTenant( Authorization, cur );
		}
	}
}
