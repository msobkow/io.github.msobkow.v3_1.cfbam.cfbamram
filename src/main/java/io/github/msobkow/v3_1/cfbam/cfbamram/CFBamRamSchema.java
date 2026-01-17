// Description: Java 25 implementation of an in-memory RAM CFBam schema.

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

import java.lang.reflect.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;
import io.github.msobkow.v3_1.cfbam.CFBamSaxLoader.*;

public class CFBamRamSchema
	extends CFBamSchema
	implements ICFBamSchema
{
		protected short nextISOCcyIdGenValue = 1;
		protected short nextISOCtryIdGenValue = 1;
		protected short nextISOLangIdGenValue = 1;
		protected short nextISOTZoneIdGenValue = 1;
		protected int nextMimeTypeIdGenValue = 1;
		protected int nextURLProtocolIdGenValue = 1;
		protected long nextClusterIdGenValue = 1;


	public CFBamRamSchema() {
		super();
		tableAtom = new CFBamRamAtomTable( this );
		tableBlobCol = new CFBamRamBlobColTable( this );
		tableBlobDef = new CFBamRamBlobDefTable( this );
		tableBlobType = new CFBamRamBlobTypeTable( this );
		tableBoolCol = new CFBamRamBoolColTable( this );
		tableBoolDef = new CFBamRamBoolDefTable( this );
		tableBoolType = new CFBamRamBoolTypeTable( this );
		tableChain = new CFBamRamChainTable( this );
		tableClearDep = new CFBamRamClearDepTable( this );
		tableClearSubDep1 = new CFBamRamClearSubDep1Table( this );
		tableClearSubDep2 = new CFBamRamClearSubDep2Table( this );
		tableClearSubDep3 = new CFBamRamClearSubDep3Table( this );
		tableClearTopDep = new CFBamRamClearTopDepTable( this );
		tableCluster = new CFBamRamClusterTable( this );
		tableDateCol = new CFBamRamDateColTable( this );
		tableDateDef = new CFBamRamDateDefTable( this );
		tableDateType = new CFBamRamDateTypeTable( this );
		tableDbKeyHash128Col = new CFBamRamDbKeyHash128ColTable( this );
		tableDbKeyHash128Def = new CFBamRamDbKeyHash128DefTable( this );
		tableDbKeyHash128Gen = new CFBamRamDbKeyHash128GenTable( this );
		tableDbKeyHash128Type = new CFBamRamDbKeyHash128TypeTable( this );
		tableDbKeyHash160Col = new CFBamRamDbKeyHash160ColTable( this );
		tableDbKeyHash160Def = new CFBamRamDbKeyHash160DefTable( this );
		tableDbKeyHash160Gen = new CFBamRamDbKeyHash160GenTable( this );
		tableDbKeyHash160Type = new CFBamRamDbKeyHash160TypeTable( this );
		tableDbKeyHash224Col = new CFBamRamDbKeyHash224ColTable( this );
		tableDbKeyHash224Def = new CFBamRamDbKeyHash224DefTable( this );
		tableDbKeyHash224Gen = new CFBamRamDbKeyHash224GenTable( this );
		tableDbKeyHash224Type = new CFBamRamDbKeyHash224TypeTable( this );
		tableDbKeyHash256Col = new CFBamRamDbKeyHash256ColTable( this );
		tableDbKeyHash256Def = new CFBamRamDbKeyHash256DefTable( this );
		tableDbKeyHash256Gen = new CFBamRamDbKeyHash256GenTable( this );
		tableDbKeyHash256Type = new CFBamRamDbKeyHash256TypeTable( this );
		tableDbKeyHash384Col = new CFBamRamDbKeyHash384ColTable( this );
		tableDbKeyHash384Def = new CFBamRamDbKeyHash384DefTable( this );
		tableDbKeyHash384Gen = new CFBamRamDbKeyHash384GenTable( this );
		tableDbKeyHash384Type = new CFBamRamDbKeyHash384TypeTable( this );
		tableDbKeyHash512Col = new CFBamRamDbKeyHash512ColTable( this );
		tableDbKeyHash512Def = new CFBamRamDbKeyHash512DefTable( this );
		tableDbKeyHash512Gen = new CFBamRamDbKeyHash512GenTable( this );
		tableDbKeyHash512Type = new CFBamRamDbKeyHash512TypeTable( this );
		tableDelDep = new CFBamRamDelDepTable( this );
		tableDelSubDep1 = new CFBamRamDelSubDep1Table( this );
		tableDelSubDep2 = new CFBamRamDelSubDep2Table( this );
		tableDelSubDep3 = new CFBamRamDelSubDep3Table( this );
		tableDelTopDep = new CFBamRamDelTopDepTable( this );
		tableDoubleCol = new CFBamRamDoubleColTable( this );
		tableDoubleDef = new CFBamRamDoubleDefTable( this );
		tableDoubleType = new CFBamRamDoubleTypeTable( this );
		tableEnumDef = new CFBamRamEnumDefTable( this );
		tableEnumTag = new CFBamRamEnumTagTable( this );
		tableEnumType = new CFBamRamEnumTypeTable( this );
		tableFloatCol = new CFBamRamFloatColTable( this );
		tableFloatDef = new CFBamRamFloatDefTable( this );
		tableFloatType = new CFBamRamFloatTypeTable( this );
		tableHostNode = new CFBamRamHostNodeTable( this );
		tableISOCcy = new CFBamRamISOCcyTable( this );
		tableISOCtry = new CFBamRamISOCtryTable( this );
		tableISOCtryCcy = new CFBamRamISOCtryCcyTable( this );
		tableISOCtryLang = new CFBamRamISOCtryLangTable( this );
		tableISOLang = new CFBamRamISOLangTable( this );
		tableISOTZone = new CFBamRamISOTZoneTable( this );
		tableId16Gen = new CFBamRamId16GenTable( this );
		tableId32Gen = new CFBamRamId32GenTable( this );
		tableId64Gen = new CFBamRamId64GenTable( this );
		tableIndex = new CFBamRamIndexTable( this );
		tableIndexCol = new CFBamRamIndexColTable( this );
		tableInt16Col = new CFBamRamInt16ColTable( this );
		tableInt16Def = new CFBamRamInt16DefTable( this );
		tableInt16Type = new CFBamRamInt16TypeTable( this );
		tableInt32Col = new CFBamRamInt32ColTable( this );
		tableInt32Def = new CFBamRamInt32DefTable( this );
		tableInt32Type = new CFBamRamInt32TypeTable( this );
		tableInt64Col = new CFBamRamInt64ColTable( this );
		tableInt64Def = new CFBamRamInt64DefTable( this );
		tableInt64Type = new CFBamRamInt64TypeTable( this );
		tableLicense = new CFBamRamLicenseTable( this );
		tableMajorVersion = new CFBamRamMajorVersionTable( this );
		tableMimeType = new CFBamRamMimeTypeTable( this );
		tableMinorVersion = new CFBamRamMinorVersionTable( this );
		tableNmTokenCol = new CFBamRamNmTokenColTable( this );
		tableNmTokenDef = new CFBamRamNmTokenDefTable( this );
		tableNmTokenType = new CFBamRamNmTokenTypeTable( this );
		tableNmTokensCol = new CFBamRamNmTokensColTable( this );
		tableNmTokensDef = new CFBamRamNmTokensDefTable( this );
		tableNmTokensType = new CFBamRamNmTokensTypeTable( this );
		tableNumberCol = new CFBamRamNumberColTable( this );
		tableNumberDef = new CFBamRamNumberDefTable( this );
		tableNumberType = new CFBamRamNumberTypeTable( this );
		tableParam = new CFBamRamParamTable( this );
		tablePopDep = new CFBamRamPopDepTable( this );
		tablePopSubDep1 = new CFBamRamPopSubDep1Table( this );
		tablePopSubDep2 = new CFBamRamPopSubDep2Table( this );
		tablePopSubDep3 = new CFBamRamPopSubDep3Table( this );
		tablePopTopDep = new CFBamRamPopTopDepTable( this );
		tableRelation = new CFBamRamRelationTable( this );
		tableRelationCol = new CFBamRamRelationColTable( this );
		tableSchemaDef = new CFBamRamSchemaDefTable( this );
		tableSchemaRef = new CFBamRamSchemaRefTable( this );
		tableScope = new CFBamRamScopeTable( this );
		tableSecDevice = new CFBamRamSecDeviceTable( this );
		tableSecGroup = new CFBamRamSecGroupTable( this );
		tableSecGrpInc = new CFBamRamSecGrpIncTable( this );
		tableSecGrpMemb = new CFBamRamSecGrpMembTable( this );
		tableSecSession = new CFBamRamSecSessionTable( this );
		tableSecUser = new CFBamRamSecUserTable( this );
		tableServerListFunc = new CFBamRamServerListFuncTable( this );
		tableServerMethod = new CFBamRamServerMethodTable( this );
		tableServerObjFunc = new CFBamRamServerObjFuncTable( this );
		tableServerProc = new CFBamRamServerProcTable( this );
		tableService = new CFBamRamServiceTable( this );
		tableServiceType = new CFBamRamServiceTypeTable( this );
		tableStringCol = new CFBamRamStringColTable( this );
		tableStringDef = new CFBamRamStringDefTable( this );
		tableStringType = new CFBamRamStringTypeTable( this );
		tableSubProject = new CFBamRamSubProjectTable( this );
		tableSysCluster = new CFBamRamSysClusterTable( this );
		tableTSecGroup = new CFBamRamTSecGroupTable( this );
		tableTSecGrpInc = new CFBamRamTSecGrpIncTable( this );
		tableTSecGrpMemb = new CFBamRamTSecGrpMembTable( this );
		tableTZDateCol = new CFBamRamTZDateColTable( this );
		tableTZDateDef = new CFBamRamTZDateDefTable( this );
		tableTZDateType = new CFBamRamTZDateTypeTable( this );
		tableTZTimeCol = new CFBamRamTZTimeColTable( this );
		tableTZTimeDef = new CFBamRamTZTimeDefTable( this );
		tableTZTimeType = new CFBamRamTZTimeTypeTable( this );
		tableTZTimestampCol = new CFBamRamTZTimestampColTable( this );
		tableTZTimestampDef = new CFBamRamTZTimestampDefTable( this );
		tableTZTimestampType = new CFBamRamTZTimestampTypeTable( this );
		tableTable = new CFBamRamTableTable( this );
		tableTableCol = new CFBamRamTableColTable( this );
		tableTenant = new CFBamRamTenantTable( this );
		tableTextCol = new CFBamRamTextColTable( this );
		tableTextDef = new CFBamRamTextDefTable( this );
		tableTextType = new CFBamRamTextTypeTable( this );
		tableTimeCol = new CFBamRamTimeColTable( this );
		tableTimeDef = new CFBamRamTimeDefTable( this );
		tableTimeType = new CFBamRamTimeTypeTable( this );
		tableTimestampCol = new CFBamRamTimestampColTable( this );
		tableTimestampDef = new CFBamRamTimestampDefTable( this );
		tableTimestampType = new CFBamRamTimestampTypeTable( this );
		tableTld = new CFBamRamTldTable( this );
		tableTokenCol = new CFBamRamTokenColTable( this );
		tableTokenDef = new CFBamRamTokenDefTable( this );
		tableTokenType = new CFBamRamTokenTypeTable( this );
		tableTopDomain = new CFBamRamTopDomainTable( this );
		tableTopProject = new CFBamRamTopProjectTable( this );
		tableUInt16Col = new CFBamRamUInt16ColTable( this );
		tableUInt16Def = new CFBamRamUInt16DefTable( this );
		tableUInt16Type = new CFBamRamUInt16TypeTable( this );
		tableUInt32Col = new CFBamRamUInt32ColTable( this );
		tableUInt32Def = new CFBamRamUInt32DefTable( this );
		tableUInt32Type = new CFBamRamUInt32TypeTable( this );
		tableUInt64Col = new CFBamRamUInt64ColTable( this );
		tableUInt64Def = new CFBamRamUInt64DefTable( this );
		tableUInt64Type = new CFBamRamUInt64TypeTable( this );
		tableURLProtocol = new CFBamRamURLProtocolTable( this );
		tableUuid6Col = new CFBamRamUuid6ColTable( this );
		tableUuid6Def = new CFBamRamUuid6DefTable( this );
		tableUuid6Gen = new CFBamRamUuid6GenTable( this );
		tableUuid6Type = new CFBamRamUuid6TypeTable( this );
		tableUuidCol = new CFBamRamUuidColTable( this );
		tableUuidDef = new CFBamRamUuidDefTable( this );
		tableUuidGen = new CFBamRamUuidGenTable( this );
		tableUuidType = new CFBamRamUuidTypeTable( this );
		tableValue = new CFBamRamValueTable( this );
	}

	protected boolean sessConnected = false;

	public boolean isConnected() {
		return( sessConnected );
	}

	public boolean connect() {
		if( sessConnected ) {
			return( false );
		}
		else {
			sessConnected = true;
			tranOpen = false;
			return( true );
		}
	}

	public boolean connect( String username, String password ) {
		final String S_ProcName = "connect";
		if( ( username == null ) || ( username.length() <= 0 ) ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"username" );
		}
		if( password == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				2,
				"password" );
		}
		if( ! username.equals( "system" ) ) {
			throw new CFLibRuntimeException( getClass(),
				S_ProcName,
				"Only 'system' is authorized to use a RAM database" );
		}
		if( sessConnected ) {
			return( false );
		}
		else {
			sessConnected = true;
			tranOpen = false;
			return( true );
		}
	}

	public boolean connect( String loginId, String password, String clusterName, String tenantName ) {
		final String S_ProcName = "connect";
		if( ( loginId == null ) || ( loginId.length() <= 0 ) ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"loginId" );
		}
		if( password == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				2,
				"password" );
		}
		if( clusterName == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				3,
				"clusterName" );
		}
		if( tenantName == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				4,
				"tenantName" );
		}
		if( ! loginId.equals( "system" ) ) {
			throw new CFLibRuntimeException( getClass(),
				S_ProcName,
				"Only 'system' is authorized to use a RAM database" );
		}
		if( ! clusterName.equals( "system" ) ) {
			throw new CFLibRuntimeException( getClass(),
				S_ProcName,
				"Only the 'system' Cluster is authorized to use a RAM database" );
		}
		if( ! tenantName.equals( "system" ) ) {
			throw new CFLibRuntimeException( getClass(),
				S_ProcName,
				"Only the 'system' Tenant is authorized to use a RAM database" );
		}
		if( sessConnected ) {
			return( false );
		}
		else {
			sessConnected = true;
			tranOpen = false;
			return( true );
		}
	}

	public void disconnect( boolean doCommit ) {
		tranOpen = false;
		sessConnected = false;
	}

	//	Transactions are not supported, so pretend there is always one open

	protected boolean tranOpen = false;

	public boolean isTransactionOpen() {
		return( tranOpen );
	}

	public boolean beginTransaction() {
		if( tranOpen ) {
			return( false );
		}
		tranOpen = true;
		return( true );
	}

	public void commit() {
		tranOpen = false;
	}

	public void rollback() {
		tranOpen = false;
	}

	public ICFBamSchema newSchema() {
		throw new CFLibMustOverrideException( getClass(), "newSchema" );
	}

	public short nextISOCcyIdGen() {
		short next = nextISOCcyIdGenValue++;
		return( next );
	}

	public short nextISOCtryIdGen() {
		short next = nextISOCtryIdGenValue++;
		return( next );
	}

	public short nextISOLangIdGen() {
		short next = nextISOLangIdGenValue++;
		return( next );
	}

	public short nextISOTZoneIdGen() {
		short next = nextISOTZoneIdGenValue++;
		return( next );
	}

	public int nextMimeTypeIdGen() {
		int next = nextMimeTypeIdGenValue++;
		return( next );
	}

	public int nextURLProtocolIdGen() {
		int next = nextURLProtocolIdGenValue++;
		return( next );
	}

	public long nextClusterIdGen() {
		long next = nextClusterIdGenValue++;
		return( next );
	}

	public void releasePreparedStatements() {
	}

	public String fileImport( CFSecAuthorization Authorization,
		String fileName,
		String fileContent )
	{
		final String S_ProcName = "fileImport";
		if( ( fileName == null ) || ( fileName.length() <= 0 ) ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"fileName" );
		}
		if( ( fileContent == null ) || ( fileContent.length() <= 0 ) ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				2,
				"fileContent" );
		}

		CFBamSaxLoader saxLoader = new CFBamSaxLoader();
		ICFBamSchemaObj schemaObj = new CFBamSchemaObj();
		schemaObj.setBackingStore( this );
		saxLoader.setSchemaObj( schemaObj );
		ICFSecClusterObj useCluster = schemaObj.getClusterTableObj().readClusterByIdIdx( Authorization.getSecClusterId() );
		ICFSecTenantObj useTenant = schemaObj.getTenantTableObj().readTenantByIdIdx( Authorization.getSecTenantId() );
		CFLibCachedMessageLog runlog = new CFLibCachedMessageLog();
		saxLoader.setLog( runlog );
		saxLoader.setUseCluster( useCluster );
		saxLoader.setUseTenant( useTenant );
		saxLoader.parseStringContents( fileContent );
		String logFileContent = runlog.getCacheContents();
		if( logFileContent == null ) {
			logFileContent = "";
		}

		return( logFileContent );
	}
}
