
// Description: Java 25 in-memory RAM DbIO implementation for Atom.

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
 *	CFBamRamAtomTable in-memory RAM DbIO implementation
 *	for Atom.
 */
public class CFBamRamAtomTable
	implements ICFBamAtomTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffAtom > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffAtom >();

	public CFBamRamAtomTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamAtom createAtom( ICFSecAuthorization Authorization,
		ICFBamAtom iBuff )
	{
		final String S_ProcName = "createAtom";
		
		CFBamBuffAtom Buff = (CFBamBuffAtom)(schema.getTableValue().createValue( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamAtom.CLASS_CODE) {
				CFBamBuffAtom retbuff = ((CFBamBuffAtom)(schema.getFactoryAtom().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBlobDef.CLASS_CODE) {
				CFBamBuffBlobDef retbuff = ((CFBamBuffBlobDef)(schema.getFactoryBlobDef().newRec()));
				retbuff.set((ICFBamBlobDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBlobType.CLASS_CODE) {
				CFBamBuffBlobType retbuff = ((CFBamBuffBlobType)(schema.getFactoryBlobType().newRec()));
				retbuff.set((ICFBamBlobType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBlobCol.CLASS_CODE) {
				CFBamBuffBlobCol retbuff = ((CFBamBuffBlobCol)(schema.getFactoryBlobCol().newRec()));
				retbuff.set((ICFBamBlobCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBoolDef.CLASS_CODE) {
				CFBamBuffBoolDef retbuff = ((CFBamBuffBoolDef)(schema.getFactoryBoolDef().newRec()));
				retbuff.set((ICFBamBoolDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBoolType.CLASS_CODE) {
				CFBamBuffBoolType retbuff = ((CFBamBuffBoolType)(schema.getFactoryBoolType().newRec()));
				retbuff.set((ICFBamBoolType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBoolCol.CLASS_CODE) {
				CFBamBuffBoolCol retbuff = ((CFBamBuffBoolCol)(schema.getFactoryBoolCol().newRec()));
				retbuff.set((ICFBamBoolCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDateDef.CLASS_CODE) {
				CFBamBuffDateDef retbuff = ((CFBamBuffDateDef)(schema.getFactoryDateDef().newRec()));
				retbuff.set((ICFBamDateDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDateType.CLASS_CODE) {
				CFBamBuffDateType retbuff = ((CFBamBuffDateType)(schema.getFactoryDateType().newRec()));
				retbuff.set((ICFBamDateType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDateCol.CLASS_CODE) {
				CFBamBuffDateCol retbuff = ((CFBamBuffDateCol)(schema.getFactoryDateCol().newRec()));
				retbuff.set((ICFBamDateCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDoubleDef.CLASS_CODE) {
				CFBamBuffDoubleDef retbuff = ((CFBamBuffDoubleDef)(schema.getFactoryDoubleDef().newRec()));
				retbuff.set((ICFBamDoubleDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDoubleType.CLASS_CODE) {
				CFBamBuffDoubleType retbuff = ((CFBamBuffDoubleType)(schema.getFactoryDoubleType().newRec()));
				retbuff.set((ICFBamDoubleType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDoubleCol.CLASS_CODE) {
				CFBamBuffDoubleCol retbuff = ((CFBamBuffDoubleCol)(schema.getFactoryDoubleCol().newRec()));
				retbuff.set((ICFBamDoubleCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamFloatDef.CLASS_CODE) {
				CFBamBuffFloatDef retbuff = ((CFBamBuffFloatDef)(schema.getFactoryFloatDef().newRec()));
				retbuff.set((ICFBamFloatDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamFloatType.CLASS_CODE) {
				CFBamBuffFloatType retbuff = ((CFBamBuffFloatType)(schema.getFactoryFloatType().newRec()));
				retbuff.set((ICFBamFloatType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamFloatCol.CLASS_CODE) {
				CFBamBuffFloatCol retbuff = ((CFBamBuffFloatCol)(schema.getFactoryFloatCol().newRec()));
				retbuff.set((ICFBamFloatCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt16Def.CLASS_CODE) {
				CFBamBuffInt16Def retbuff = ((CFBamBuffInt16Def)(schema.getFactoryInt16Def().newRec()));
				retbuff.set((ICFBamInt16Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt16Type.CLASS_CODE) {
				CFBamBuffInt16Type retbuff = ((CFBamBuffInt16Type)(schema.getFactoryInt16Type().newRec()));
				retbuff.set((ICFBamInt16Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamId16Gen.CLASS_CODE) {
				CFBamBuffId16Gen retbuff = ((CFBamBuffId16Gen)(schema.getFactoryId16Gen().newRec()));
				retbuff.set((ICFBamId16Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamEnumDef.CLASS_CODE) {
				CFBamBuffEnumDef retbuff = ((CFBamBuffEnumDef)(schema.getFactoryEnumDef().newRec()));
				retbuff.set((ICFBamEnumDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamEnumType.CLASS_CODE) {
				CFBamBuffEnumType retbuff = ((CFBamBuffEnumType)(schema.getFactoryEnumType().newRec()));
				retbuff.set((ICFBamEnumType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt16Col.CLASS_CODE) {
				CFBamBuffInt16Col retbuff = ((CFBamBuffInt16Col)(schema.getFactoryInt16Col().newRec()));
				retbuff.set((ICFBamInt16Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt32Def.CLASS_CODE) {
				CFBamBuffInt32Def retbuff = ((CFBamBuffInt32Def)(schema.getFactoryInt32Def().newRec()));
				retbuff.set((ICFBamInt32Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt32Type.CLASS_CODE) {
				CFBamBuffInt32Type retbuff = ((CFBamBuffInt32Type)(schema.getFactoryInt32Type().newRec()));
				retbuff.set((ICFBamInt32Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamId32Gen.CLASS_CODE) {
				CFBamBuffId32Gen retbuff = ((CFBamBuffId32Gen)(schema.getFactoryId32Gen().newRec()));
				retbuff.set((ICFBamId32Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt32Col.CLASS_CODE) {
				CFBamBuffInt32Col retbuff = ((CFBamBuffInt32Col)(schema.getFactoryInt32Col().newRec()));
				retbuff.set((ICFBamInt32Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt64Def.CLASS_CODE) {
				CFBamBuffInt64Def retbuff = ((CFBamBuffInt64Def)(schema.getFactoryInt64Def().newRec()));
				retbuff.set((ICFBamInt64Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt64Type.CLASS_CODE) {
				CFBamBuffInt64Type retbuff = ((CFBamBuffInt64Type)(schema.getFactoryInt64Type().newRec()));
				retbuff.set((ICFBamInt64Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamId64Gen.CLASS_CODE) {
				CFBamBuffId64Gen retbuff = ((CFBamBuffId64Gen)(schema.getFactoryId64Gen().newRec()));
				retbuff.set((ICFBamId64Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt64Col.CLASS_CODE) {
				CFBamBuffInt64Col retbuff = ((CFBamBuffInt64Col)(schema.getFactoryInt64Col().newRec()));
				retbuff.set((ICFBamInt64Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokenDef.CLASS_CODE) {
				CFBamBuffNmTokenDef retbuff = ((CFBamBuffNmTokenDef)(schema.getFactoryNmTokenDef().newRec()));
				retbuff.set((ICFBamNmTokenDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokenType.CLASS_CODE) {
				CFBamBuffNmTokenType retbuff = ((CFBamBuffNmTokenType)(schema.getFactoryNmTokenType().newRec()));
				retbuff.set((ICFBamNmTokenType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokenCol.CLASS_CODE) {
				CFBamBuffNmTokenCol retbuff = ((CFBamBuffNmTokenCol)(schema.getFactoryNmTokenCol().newRec()));
				retbuff.set((ICFBamNmTokenCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokensDef.CLASS_CODE) {
				CFBamBuffNmTokensDef retbuff = ((CFBamBuffNmTokensDef)(schema.getFactoryNmTokensDef().newRec()));
				retbuff.set((ICFBamNmTokensDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokensType.CLASS_CODE) {
				CFBamBuffNmTokensType retbuff = ((CFBamBuffNmTokensType)(schema.getFactoryNmTokensType().newRec()));
				retbuff.set((ICFBamNmTokensType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokensCol.CLASS_CODE) {
				CFBamBuffNmTokensCol retbuff = ((CFBamBuffNmTokensCol)(schema.getFactoryNmTokensCol().newRec()));
				retbuff.set((ICFBamNmTokensCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNumberDef.CLASS_CODE) {
				CFBamBuffNumberDef retbuff = ((CFBamBuffNumberDef)(schema.getFactoryNumberDef().newRec()));
				retbuff.set((ICFBamNumberDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNumberType.CLASS_CODE) {
				CFBamBuffNumberType retbuff = ((CFBamBuffNumberType)(schema.getFactoryNumberType().newRec()));
				retbuff.set((ICFBamNumberType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNumberCol.CLASS_CODE) {
				CFBamBuffNumberCol retbuff = ((CFBamBuffNumberCol)(schema.getFactoryNumberCol().newRec()));
				retbuff.set((ICFBamNumberCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Def.CLASS_CODE) {
				CFBamBuffDbKeyHash128Def retbuff = ((CFBamBuffDbKeyHash128Def)(schema.getFactoryDbKeyHash128Def().newRec()));
				retbuff.set((ICFBamDbKeyHash128Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Col.CLASS_CODE) {
				CFBamBuffDbKeyHash128Col retbuff = ((CFBamBuffDbKeyHash128Col)(schema.getFactoryDbKeyHash128Col().newRec()));
				retbuff.set((ICFBamDbKeyHash128Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Type.CLASS_CODE) {
				CFBamBuffDbKeyHash128Type retbuff = ((CFBamBuffDbKeyHash128Type)(schema.getFactoryDbKeyHash128Type().newRec()));
				retbuff.set((ICFBamDbKeyHash128Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash128Gen retbuff = ((CFBamBuffDbKeyHash128Gen)(schema.getFactoryDbKeyHash128Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash128Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Def.CLASS_CODE) {
				CFBamBuffDbKeyHash160Def retbuff = ((CFBamBuffDbKeyHash160Def)(schema.getFactoryDbKeyHash160Def().newRec()));
				retbuff.set((ICFBamDbKeyHash160Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Col.CLASS_CODE) {
				CFBamBuffDbKeyHash160Col retbuff = ((CFBamBuffDbKeyHash160Col)(schema.getFactoryDbKeyHash160Col().newRec()));
				retbuff.set((ICFBamDbKeyHash160Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Type.CLASS_CODE) {
				CFBamBuffDbKeyHash160Type retbuff = ((CFBamBuffDbKeyHash160Type)(schema.getFactoryDbKeyHash160Type().newRec()));
				retbuff.set((ICFBamDbKeyHash160Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash160Gen retbuff = ((CFBamBuffDbKeyHash160Gen)(schema.getFactoryDbKeyHash160Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash160Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Def.CLASS_CODE) {
				CFBamBuffDbKeyHash224Def retbuff = ((CFBamBuffDbKeyHash224Def)(schema.getFactoryDbKeyHash224Def().newRec()));
				retbuff.set((ICFBamDbKeyHash224Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Col.CLASS_CODE) {
				CFBamBuffDbKeyHash224Col retbuff = ((CFBamBuffDbKeyHash224Col)(schema.getFactoryDbKeyHash224Col().newRec()));
				retbuff.set((ICFBamDbKeyHash224Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Type.CLASS_CODE) {
				CFBamBuffDbKeyHash224Type retbuff = ((CFBamBuffDbKeyHash224Type)(schema.getFactoryDbKeyHash224Type().newRec()));
				retbuff.set((ICFBamDbKeyHash224Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash224Gen retbuff = ((CFBamBuffDbKeyHash224Gen)(schema.getFactoryDbKeyHash224Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash224Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Def.CLASS_CODE) {
				CFBamBuffDbKeyHash256Def retbuff = ((CFBamBuffDbKeyHash256Def)(schema.getFactoryDbKeyHash256Def().newRec()));
				retbuff.set((ICFBamDbKeyHash256Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Col.CLASS_CODE) {
				CFBamBuffDbKeyHash256Col retbuff = ((CFBamBuffDbKeyHash256Col)(schema.getFactoryDbKeyHash256Col().newRec()));
				retbuff.set((ICFBamDbKeyHash256Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Type.CLASS_CODE) {
				CFBamBuffDbKeyHash256Type retbuff = ((CFBamBuffDbKeyHash256Type)(schema.getFactoryDbKeyHash256Type().newRec()));
				retbuff.set((ICFBamDbKeyHash256Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash256Gen retbuff = ((CFBamBuffDbKeyHash256Gen)(schema.getFactoryDbKeyHash256Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash256Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Def.CLASS_CODE) {
				CFBamBuffDbKeyHash384Def retbuff = ((CFBamBuffDbKeyHash384Def)(schema.getFactoryDbKeyHash384Def().newRec()));
				retbuff.set((ICFBamDbKeyHash384Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Col.CLASS_CODE) {
				CFBamBuffDbKeyHash384Col retbuff = ((CFBamBuffDbKeyHash384Col)(schema.getFactoryDbKeyHash384Col().newRec()));
				retbuff.set((ICFBamDbKeyHash384Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Type.CLASS_CODE) {
				CFBamBuffDbKeyHash384Type retbuff = ((CFBamBuffDbKeyHash384Type)(schema.getFactoryDbKeyHash384Type().newRec()));
				retbuff.set((ICFBamDbKeyHash384Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash384Gen retbuff = ((CFBamBuffDbKeyHash384Gen)(schema.getFactoryDbKeyHash384Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash384Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Def.CLASS_CODE) {
				CFBamBuffDbKeyHash512Def retbuff = ((CFBamBuffDbKeyHash512Def)(schema.getFactoryDbKeyHash512Def().newRec()));
				retbuff.set((ICFBamDbKeyHash512Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Col.CLASS_CODE) {
				CFBamBuffDbKeyHash512Col retbuff = ((CFBamBuffDbKeyHash512Col)(schema.getFactoryDbKeyHash512Col().newRec()));
				retbuff.set((ICFBamDbKeyHash512Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Type.CLASS_CODE) {
				CFBamBuffDbKeyHash512Type retbuff = ((CFBamBuffDbKeyHash512Type)(schema.getFactoryDbKeyHash512Type().newRec()));
				retbuff.set((ICFBamDbKeyHash512Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash512Gen retbuff = ((CFBamBuffDbKeyHash512Gen)(schema.getFactoryDbKeyHash512Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash512Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamStringDef.CLASS_CODE) {
				CFBamBuffStringDef retbuff = ((CFBamBuffStringDef)(schema.getFactoryStringDef().newRec()));
				retbuff.set((ICFBamStringDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamStringType.CLASS_CODE) {
				CFBamBuffStringType retbuff = ((CFBamBuffStringType)(schema.getFactoryStringType().newRec()));
				retbuff.set((ICFBamStringType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamStringCol.CLASS_CODE) {
				CFBamBuffStringCol retbuff = ((CFBamBuffStringCol)(schema.getFactoryStringCol().newRec()));
				retbuff.set((ICFBamStringCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZDateDef.CLASS_CODE) {
				CFBamBuffTZDateDef retbuff = ((CFBamBuffTZDateDef)(schema.getFactoryTZDateDef().newRec()));
				retbuff.set((ICFBamTZDateDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZDateType.CLASS_CODE) {
				CFBamBuffTZDateType retbuff = ((CFBamBuffTZDateType)(schema.getFactoryTZDateType().newRec()));
				retbuff.set((ICFBamTZDateType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZDateCol.CLASS_CODE) {
				CFBamBuffTZDateCol retbuff = ((CFBamBuffTZDateCol)(schema.getFactoryTZDateCol().newRec()));
				retbuff.set((ICFBamTZDateCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimeDef.CLASS_CODE) {
				CFBamBuffTZTimeDef retbuff = ((CFBamBuffTZTimeDef)(schema.getFactoryTZTimeDef().newRec()));
				retbuff.set((ICFBamTZTimeDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimeType.CLASS_CODE) {
				CFBamBuffTZTimeType retbuff = ((CFBamBuffTZTimeType)(schema.getFactoryTZTimeType().newRec()));
				retbuff.set((ICFBamTZTimeType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimeCol.CLASS_CODE) {
				CFBamBuffTZTimeCol retbuff = ((CFBamBuffTZTimeCol)(schema.getFactoryTZTimeCol().newRec()));
				retbuff.set((ICFBamTZTimeCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimestampDef.CLASS_CODE) {
				CFBamBuffTZTimestampDef retbuff = ((CFBamBuffTZTimestampDef)(schema.getFactoryTZTimestampDef().newRec()));
				retbuff.set((ICFBamTZTimestampDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimestampType.CLASS_CODE) {
				CFBamBuffTZTimestampType retbuff = ((CFBamBuffTZTimestampType)(schema.getFactoryTZTimestampType().newRec()));
				retbuff.set((ICFBamTZTimestampType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimestampCol.CLASS_CODE) {
				CFBamBuffTZTimestampCol retbuff = ((CFBamBuffTZTimestampCol)(schema.getFactoryTZTimestampCol().newRec()));
				retbuff.set((ICFBamTZTimestampCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTextDef.CLASS_CODE) {
				CFBamBuffTextDef retbuff = ((CFBamBuffTextDef)(schema.getFactoryTextDef().newRec()));
				retbuff.set((ICFBamTextDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTextType.CLASS_CODE) {
				CFBamBuffTextType retbuff = ((CFBamBuffTextType)(schema.getFactoryTextType().newRec()));
				retbuff.set((ICFBamTextType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTextCol.CLASS_CODE) {
				CFBamBuffTextCol retbuff = ((CFBamBuffTextCol)(schema.getFactoryTextCol().newRec()));
				retbuff.set((ICFBamTextCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimeDef.CLASS_CODE) {
				CFBamBuffTimeDef retbuff = ((CFBamBuffTimeDef)(schema.getFactoryTimeDef().newRec()));
				retbuff.set((ICFBamTimeDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimeType.CLASS_CODE) {
				CFBamBuffTimeType retbuff = ((CFBamBuffTimeType)(schema.getFactoryTimeType().newRec()));
				retbuff.set((ICFBamTimeType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimeCol.CLASS_CODE) {
				CFBamBuffTimeCol retbuff = ((CFBamBuffTimeCol)(schema.getFactoryTimeCol().newRec()));
				retbuff.set((ICFBamTimeCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimestampDef.CLASS_CODE) {
				CFBamBuffTimestampDef retbuff = ((CFBamBuffTimestampDef)(schema.getFactoryTimestampDef().newRec()));
				retbuff.set((ICFBamTimestampDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimestampType.CLASS_CODE) {
				CFBamBuffTimestampType retbuff = ((CFBamBuffTimestampType)(schema.getFactoryTimestampType().newRec()));
				retbuff.set((ICFBamTimestampType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimestampCol.CLASS_CODE) {
				CFBamBuffTimestampCol retbuff = ((CFBamBuffTimestampCol)(schema.getFactoryTimestampCol().newRec()));
				retbuff.set((ICFBamTimestampCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTokenDef.CLASS_CODE) {
				CFBamBuffTokenDef retbuff = ((CFBamBuffTokenDef)(schema.getFactoryTokenDef().newRec()));
				retbuff.set((ICFBamTokenDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTokenType.CLASS_CODE) {
				CFBamBuffTokenType retbuff = ((CFBamBuffTokenType)(schema.getFactoryTokenType().newRec()));
				retbuff.set((ICFBamTokenType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTokenCol.CLASS_CODE) {
				CFBamBuffTokenCol retbuff = ((CFBamBuffTokenCol)(schema.getFactoryTokenCol().newRec()));
				retbuff.set((ICFBamTokenCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt16Def.CLASS_CODE) {
				CFBamBuffUInt16Def retbuff = ((CFBamBuffUInt16Def)(schema.getFactoryUInt16Def().newRec()));
				retbuff.set((ICFBamUInt16Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt16Type.CLASS_CODE) {
				CFBamBuffUInt16Type retbuff = ((CFBamBuffUInt16Type)(schema.getFactoryUInt16Type().newRec()));
				retbuff.set((ICFBamUInt16Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt16Col.CLASS_CODE) {
				CFBamBuffUInt16Col retbuff = ((CFBamBuffUInt16Col)(schema.getFactoryUInt16Col().newRec()));
				retbuff.set((ICFBamUInt16Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt32Def.CLASS_CODE) {
				CFBamBuffUInt32Def retbuff = ((CFBamBuffUInt32Def)(schema.getFactoryUInt32Def().newRec()));
				retbuff.set((ICFBamUInt32Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt32Type.CLASS_CODE) {
				CFBamBuffUInt32Type retbuff = ((CFBamBuffUInt32Type)(schema.getFactoryUInt32Type().newRec()));
				retbuff.set((ICFBamUInt32Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt32Col.CLASS_CODE) {
				CFBamBuffUInt32Col retbuff = ((CFBamBuffUInt32Col)(schema.getFactoryUInt32Col().newRec()));
				retbuff.set((ICFBamUInt32Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt64Def.CLASS_CODE) {
				CFBamBuffUInt64Def retbuff = ((CFBamBuffUInt64Def)(schema.getFactoryUInt64Def().newRec()));
				retbuff.set((ICFBamUInt64Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt64Type.CLASS_CODE) {
				CFBamBuffUInt64Type retbuff = ((CFBamBuffUInt64Type)(schema.getFactoryUInt64Type().newRec()));
				retbuff.set((ICFBamUInt64Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt64Col.CLASS_CODE) {
				CFBamBuffUInt64Col retbuff = ((CFBamBuffUInt64Col)(schema.getFactoryUInt64Col().newRec()));
				retbuff.set((ICFBamUInt64Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidDef.CLASS_CODE) {
				CFBamBuffUuidDef retbuff = ((CFBamBuffUuidDef)(schema.getFactoryUuidDef().newRec()));
				retbuff.set((ICFBamUuidDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidType.CLASS_CODE) {
				CFBamBuffUuidType retbuff = ((CFBamBuffUuidType)(schema.getFactoryUuidType().newRec()));
				retbuff.set((ICFBamUuidType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidGen.CLASS_CODE) {
				CFBamBuffUuidGen retbuff = ((CFBamBuffUuidGen)(schema.getFactoryUuidGen().newRec()));
				retbuff.set((ICFBamUuidGen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidCol.CLASS_CODE) {
				CFBamBuffUuidCol retbuff = ((CFBamBuffUuidCol)(schema.getFactoryUuidCol().newRec()));
				retbuff.set((ICFBamUuidCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Def.CLASS_CODE) {
				CFBamBuffUuid6Def retbuff = ((CFBamBuffUuid6Def)(schema.getFactoryUuid6Def().newRec()));
				retbuff.set((ICFBamUuid6Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Type.CLASS_CODE) {
				CFBamBuffUuid6Type retbuff = ((CFBamBuffUuid6Type)(schema.getFactoryUuid6Type().newRec()));
				retbuff.set((ICFBamUuid6Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Gen.CLASS_CODE) {
				CFBamBuffUuid6Gen retbuff = ((CFBamBuffUuid6Gen)(schema.getFactoryUuid6Gen().newRec()));
				retbuff.set((ICFBamUuid6Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Col.CLASS_CODE) {
				CFBamBuffUuid6Col retbuff = ((CFBamBuffUuid6Col)(schema.getFactoryUuid6Col().newRec()));
				retbuff.set((ICFBamUuid6Col)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-create-buff-cloning-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamAtom readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamAtom.readDerived";
		ICFBamAtom buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamAtom lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamAtom.readDerived";
		ICFBamAtom buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamAtom[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamAtom.readAllDerived";
		ICFBamAtom[] retList = new ICFBamAtom[ dictByPKey.values().size() ];
		Iterator< ICFBamAtom > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamAtom readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByUNameIdx";
		ICFBamValue buff = schema.getTableValue().readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamAtom ) {
			return( (ICFBamAtom)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamAtom[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByScopeIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamAtom ) ) {
					filteredList.add( (ICFBamAtom)buff );
				}
			}
			return( filteredList.toArray( new ICFBamAtom[0] ) );
		}
	}

	public ICFBamAtom[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByDefSchemaIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamAtom ) ) {
					filteredList.add( (ICFBamAtom)buff );
				}
			}
			return( filteredList.toArray( new ICFBamAtom[0] ) );
		}
	}

	public ICFBamAtom[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByPrevIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByPrevIdx( Authorization,
			PrevId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamAtom ) ) {
					filteredList.add( (ICFBamAtom)buff );
				}
			}
			return( filteredList.toArray( new ICFBamAtom[0] ) );
		}
	}

	public ICFBamAtom[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByNextIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByNextIdx( Authorization,
			NextId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamAtom ) ) {
					filteredList.add( (ICFBamAtom)buff );
				}
			}
			return( filteredList.toArray( new ICFBamAtom[0] ) );
		}
	}

	public ICFBamAtom[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContPrevIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamAtom ) ) {
					filteredList.add( (ICFBamAtom)buff );
				}
			}
			return( filteredList.toArray( new ICFBamAtom[0] ) );
		}
	}

	public ICFBamAtom[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContNextIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamAtom ) ) {
					filteredList.add( (ICFBamAtom)buff );
				}
			}
			return( filteredList.toArray( new ICFBamAtom[0] ) );
		}
	}

	public ICFBamAtom readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByIdIdx() ";
		ICFBamAtom buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamAtom readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamAtom.readBuff";
		ICFBamAtom buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamAtom.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamAtom lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamAtom buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamAtom.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamAtom[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamAtom.readAllBuff";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamAtom.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	public ICFBamAtom readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByIdIdx() ";
		ICFBamAtom buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamAtom)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamAtom readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByUNameIdx() ";
		ICFBamAtom buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamAtom)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamAtom[] readBuffByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByScopeIdx() ";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamAtom)buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	public ICFBamAtom[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByDefSchemaIdx() ";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamAtom)buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	public ICFBamAtom[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByPrevIdx() ";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamAtom)buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	public ICFBamAtom[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByNextIdx() ";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamAtom)buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	public ICFBamAtom[] readBuffByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByContPrevIdx() ";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamAtom)buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	public ICFBamAtom[] readBuffByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readBuffByContNextIdx() ";
		ICFBamAtom buff;
		ArrayList<ICFBamAtom> filteredList = new ArrayList<ICFBamAtom>();
		ICFBamAtom[] buffList = readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamAtom)buff );
			}
		}
		return( filteredList.toArray( new ICFBamAtom[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamAtom moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamValue grandprev = null;
		ICFBamValue prev = null;
		ICFBamValue cur = null;
		ICFBamValue next = null;

		cur = schema.getTableValue().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamValueByIdIdxKey",
				"CFBamValueByIdIdxKey",
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamAtomBuff)cur );
		}

		prev = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamValueByIdIdxKey",
				"CFBamValueByIdIdxKey",
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableValue().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamValueByIdIdxKey",
					"CFBamValueByIdIdxKey",
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamValueByIdIdxKey",
					"CFBamValueByIdIdxKey",
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		ICFBamValue newInstance;
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		ICFBamValue editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamValueBuff editCur = newInstance;
		editCur.set( cur );

		ICFBamValue editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		ICFBamValue editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editNext = newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalNextId( cur.getRequiredId() );
			editCur.setOptionalPrevId( grandprev.getRequiredId() );
		}
		else {
			editCur.setOptionalPrevId( null );
		}

			editPrev.setOptionalPrevId( cur.getRequiredId() );

			editCur.setOptionalNextId( prev.getRequiredId() );

		if( next != null ) {
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editPrev.setOptionalNextId( null );
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editGrandprev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editGrandprev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editGrandprev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editGrandprev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editGrandprev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editGrandprev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editGrandprev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-grand-prev-", "Not " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editPrev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editPrev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editPrev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editPrev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editPrev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editPrev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editPrev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editPrev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editPrev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editPrev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editPrev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editPrev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editPrev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editPrev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editPrev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editPrev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editPrev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editPrev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editPrev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editPrev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editPrev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editPrev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editPrev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editPrev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editPrev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editPrev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editPrev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editPrev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editPrev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editPrev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-prev-", "Not " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editCur );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editCur );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editCur );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editCur );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editCur );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editCur );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editCur );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editCur );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editCur );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editCur );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editCur );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editCur );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editCur );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editCur );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editCur );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editCur );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editCur );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editCur );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editCur );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editCur );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editCur );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editCur );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editCur );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editCur );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editCur );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editCur );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editCur );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editCur );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editCur );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editCur );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editCur );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editCur );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editCur );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editCur );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editCur );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editCur );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editCur );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editCur );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editCur );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editCur );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editCur );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editCur );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editCur );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editCur );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editCur );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editCur );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editCur );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editCur );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editCur );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editCur );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editCur );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editCur );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editCur );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editCur );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editCur );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editCur );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editCur );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editCur );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editCur );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editCur );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editCur );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editCur );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editCur );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editCur );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editCur );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editCur );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editCur );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editCur );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editCur );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editCur );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editCur );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editCur );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editCur );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editCur );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editCur );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editCur );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editCur );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-cur-", "Not " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editNext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editNext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editNext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editNext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editNext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editNext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editNext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editNext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editNext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editNext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editNext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editNext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editNext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editNext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editNext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editNext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editNext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editNext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editNext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editNext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editNext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editNext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editNext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editNext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editNext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editNext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editNext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editNext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editNext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editNext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editNext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editNext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editNext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editNext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editNext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editNext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editNext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editNext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editNext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editNext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editNext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editNext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editNext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editNext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editNext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editNext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editNext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editNext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editNext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editNext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editNext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editNext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editNext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editNext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-next-", "Not " + Integer.toString(classCode));
			}
		}

		return( (CFBamAtomBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamAtom moveBuffDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamValueBuff prev = null;
		CFBamValueBuff cur = null;
		CFBamValueBuff next = null;
		CFBamValueBuff grandnext = null;

		cur = schema.getTableValue().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamValueByIdIdxKey",
				"CFBamValueByIdIdxKey",
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamAtomBuff)cur );
		}

		next = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamValueByIdIdxKey",
				"CFBamValueByIdIdxKey",
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableValue().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamValueByIdIdxKey",
					"CFBamValueByIdIdxKey",
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamValueByIdIdxKey",
					"CFBamValueByIdIdxKey",
					"Could not locate object.prev" );
			}
		}

		integer classCode = cur.getClassCode();
		CFBamValueBuff newInstance;
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamValueBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamValueBuff editNext = newInstance;
		editNext.set( next );

		CFBamValueBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamValueBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editPrev = newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editNext.setOptionalPrevId( null );
		}

			editCur.setOptionalPrevId( next.getRequiredId() );

			editNext.setOptionalNextId( cur.getRequiredId() );

		if( editGrandnext != null ) {
			editCur.setOptionalNextId( grandnext.getRequiredId() );
			editGrandnext.setOptionalPrevId( cur.getRequiredId() );
		}
		else {
			editCur.setOptionalNextId( null );
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editPrev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editPrev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editPrev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editPrev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editPrev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editPrev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editPrev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editPrev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editPrev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editPrev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editPrev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editPrev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editPrev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editPrev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editPrev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editPrev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editPrev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editPrev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editPrev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editPrev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editPrev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editPrev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editPrev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editPrev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editPrev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editPrev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editPrev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editPrev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editPrev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editPrev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-prev-", "Not " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editCur );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editCur );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editCur );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editCur );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editCur );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editCur );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editCur );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editCur );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editCur );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editCur );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editCur );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editCur );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editCur );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editCur );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editCur );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editCur );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editCur );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editCur );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editCur );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editCur );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editCur );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editCur );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editCur );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editCur );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editCur );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editCur );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editCur );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editCur );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editCur );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editCur );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editCur );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editCur );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editCur );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editCur );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editCur );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editCur );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editCur );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editCur );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editCur );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editCur );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editCur );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editCur );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editCur );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editCur );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editCur );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editCur );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editCur );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editCur );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editCur );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editCur );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editCur );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editCur );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editCur );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editCur );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editCur );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editCur );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editCur );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editCur );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editCur );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editCur );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editCur );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editCur );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editCur );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editCur );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editCur );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editCur );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editCur );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editCur );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editCur );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editCur );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editCur );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editCur );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editCur );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editCur );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editCur );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editCur );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editCur );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editCur );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-cur-", "Not " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editNext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editNext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editNext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editNext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editNext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editNext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editNext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editNext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editNext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editNext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editNext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editNext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editNext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editNext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editNext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editNext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editNext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editNext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editNext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editNext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editNext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editNext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editNext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editNext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editNext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editNext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editNext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editNext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editNext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editNext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editNext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editNext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editNext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editNext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editNext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editNext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editNext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editNext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editNext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editNext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editNext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editNext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editNext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editNext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editNext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editNext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editNext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editNext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editNext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editNext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editNext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editNext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editNext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editNext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-next-", "Not " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editGrandnext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editGrandnext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editGrandnext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editGrandnext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editGrandnext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editGrandnext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editGrandnext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-grand-next-", "Not " + Integer.toString(classCode));
			}
		}

		return( (CFBamAtomBuff)editCur );
	}

	public ICFBamAtom updateAtom( ICFSecAuthorization Authorization,
		ICFBamAtom Buff )
	{
		ICFBamAtom repl = schema.getTableValue().updateValue( Authorization,
			Buff );
		if (repl != Buff) {
			throw new CFLibInvalidStateException(getClass(), S_ProcName, "repl != Buff", "repl != Buff");
		}
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamAtom existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateAtom",
				"Existing record not found",
				"Atom",
				pkey );
		}
		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateAtom",
						"Superclass",
						"SuperClass",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffAtom > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		return(Buff);
	}

	public void deleteAtom( ICFSecAuthorization Authorization,
		ICFBamAtom Buff )
	{
		final String S_ProcName = "CFBamRamAtomTable.deleteAtom() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryValue().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamAtom existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteAtom",
				pkey );
		}
		CFLibDbKeyHash256 varScopeId = existing.getRequiredScopeId();
		CFBamScopeBuff container = schema.getTableScope().readDerivedByIdIdx( Authorization,
			varScopeId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamValueBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableValue().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamValueBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a809" ) ) {
				editPrev = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				editPrev = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				editPrev = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				editPrev = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				editPrev = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				editPrev = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				editPrev = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				editPrev = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				editPrev = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				editPrev = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				editPrev = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				editPrev = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				editPrev = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				editPrev = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				editPrev = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				editPrev = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				editPrev = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				editPrev = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				editPrev = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				editPrev = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				editPrev = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				editPrev = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				editPrev = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				editPrev = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				editPrev = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				editPrev = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				editPrev = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				editPrev = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				editPrev = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				editPrev = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				editPrev = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				editPrev = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				editPrev = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				editPrev = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				editPrev = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				editPrev = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				editPrev = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				editPrev = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				editPrev = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				editPrev = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				editPrev = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				editPrev = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				editPrev = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				editPrev = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				editPrev = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				editPrev = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				editPrev = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				editPrev = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				editPrev = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				editPrev = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				editPrev = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				editPrev = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				editPrev = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				editPrev = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				editPrev = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				editPrev = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				editPrev = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				editPrev = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-update-prev-", "Not " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editPrev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editPrev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editPrev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editPrev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editPrev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editPrev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editPrev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editPrev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editPrev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editPrev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editPrev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editPrev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editPrev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editPrev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editPrev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editPrev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editPrev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editPrev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editPrev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editPrev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editPrev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editPrev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editPrev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editPrev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editPrev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editPrev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editPrev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editPrev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editPrev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editPrev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editPrev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editPrev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editPrev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editPrev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editPrev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editPrev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editPrev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editPrev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-edit-prev-", "Not " + Integer.toString(classCode));
			}
		}

		CFBamValueBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableValue().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamValueBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a809" ) ) {
				editNext = schema.getFactoryValue().newBuff();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				editNext = schema.getFactoryAtom().newBuff();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				editNext = schema.getFactoryBlobDef().newBuff();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				editNext = schema.getFactoryBlobType().newBuff();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				editNext = schema.getFactoryBlobCol().newBuff();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				editNext = schema.getFactoryBoolDef().newBuff();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				editNext = schema.getFactoryBoolType().newBuff();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				editNext = schema.getFactoryBoolCol().newBuff();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				editNext = schema.getFactoryDateDef().newBuff();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				editNext = schema.getFactoryDateType().newBuff();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				editNext = schema.getFactoryDateCol().newBuff();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				editNext = schema.getFactoryDoubleDef().newBuff();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				editNext = schema.getFactoryDoubleType().newBuff();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				editNext = schema.getFactoryDoubleCol().newBuff();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				editNext = schema.getFactoryFloatDef().newBuff();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				editNext = schema.getFactoryFloatType().newBuff();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				editNext = schema.getFactoryFloatCol().newBuff();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				editNext = schema.getFactoryInt16Def().newBuff();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				editNext = schema.getFactoryInt16Type().newBuff();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				editNext = schema.getFactoryId16Gen().newBuff();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				editNext = schema.getFactoryEnumDef().newBuff();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				editNext = schema.getFactoryEnumType().newBuff();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				editNext = schema.getFactoryInt16Col().newBuff();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				editNext = schema.getFactoryInt32Def().newBuff();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				editNext = schema.getFactoryInt32Type().newBuff();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				editNext = schema.getFactoryId32Gen().newBuff();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				editNext = schema.getFactoryInt32Col().newBuff();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				editNext = schema.getFactoryInt64Def().newBuff();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				editNext = schema.getFactoryInt64Type().newBuff();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				editNext = schema.getFactoryId64Gen().newBuff();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				editNext = schema.getFactoryInt64Col().newBuff();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				editNext = schema.getFactoryNmTokenDef().newBuff();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				editNext = schema.getFactoryNmTokenType().newBuff();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				editNext = schema.getFactoryNmTokenCol().newBuff();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				editNext = schema.getFactoryNmTokensDef().newBuff();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				editNext = schema.getFactoryNmTokensType().newBuff();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				editNext = schema.getFactoryNmTokensCol().newBuff();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				editNext = schema.getFactoryNumberDef().newBuff();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				editNext = schema.getFactoryNumberType().newBuff();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				editNext = schema.getFactoryNumberCol().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash128Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash128Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash128Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash128Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash160Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash160Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash160Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash160Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash224Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash224Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash224Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash224Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash256Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash256Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash256Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash256Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash384Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash384Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash384Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash384Gen().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash512Def().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash512Col().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash512Type().newBuff();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				editNext = schema.getFactoryDbKeyHash512Gen().newBuff();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				editNext = schema.getFactoryStringDef().newBuff();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				editNext = schema.getFactoryStringType().newBuff();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				editNext = schema.getFactoryStringCol().newBuff();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				editNext = schema.getFactoryTZDateDef().newBuff();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				editNext = schema.getFactoryTZDateType().newBuff();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				editNext = schema.getFactoryTZDateCol().newBuff();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				editNext = schema.getFactoryTZTimeDef().newBuff();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				editNext = schema.getFactoryTZTimeType().newBuff();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				editNext = schema.getFactoryTZTimeCol().newBuff();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				editNext = schema.getFactoryTZTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				editNext = schema.getFactoryTZTimestampType().newBuff();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				editNext = schema.getFactoryTZTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				editNext = schema.getFactoryTextDef().newBuff();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				editNext = schema.getFactoryTextType().newBuff();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				editNext = schema.getFactoryTextCol().newBuff();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				editNext = schema.getFactoryTimeDef().newBuff();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				editNext = schema.getFactoryTimeType().newBuff();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				editNext = schema.getFactoryTimeCol().newBuff();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				editNext = schema.getFactoryTimestampDef().newBuff();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				editNext = schema.getFactoryTimestampType().newBuff();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				editNext = schema.getFactoryTimestampCol().newBuff();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				editNext = schema.getFactoryTokenDef().newBuff();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				editNext = schema.getFactoryTokenType().newBuff();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				editNext = schema.getFactoryTokenCol().newBuff();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				editNext = schema.getFactoryUInt16Def().newBuff();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				editNext = schema.getFactoryUInt16Type().newBuff();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				editNext = schema.getFactoryUInt16Col().newBuff();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				editNext = schema.getFactoryUInt32Def().newBuff();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				editNext = schema.getFactoryUInt32Type().newBuff();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				editNext = schema.getFactoryUInt32Col().newBuff();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				editNext = schema.getFactoryUInt64Def().newBuff();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				editNext = schema.getFactoryUInt64Type().newBuff();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				editNext = schema.getFactoryUInt64Col().newBuff();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				editNext = schema.getFactoryUuidDef().newBuff();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				editNext = schema.getFactoryUuidType().newBuff();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				editNext = schema.getFactoryUuidGen().newBuff();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				editNext = schema.getFactoryUuidCol().newBuff();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				editNext = schema.getFactoryUuid6Def().newBuff();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				editNext = schema.getFactoryUuid6Type().newBuff();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				editNext = schema.getFactoryUuid6Gen().newBuff();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				editNext = schema.getFactoryUuid6Col().newBuff();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				editNext = schema.getFactoryTableCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-update-next-", "Not " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a809" ) ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamAtomBuff)editNext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBlobDefBuff)editNext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBlobTypeBuff)editNext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBlobColBuff)editNext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBoolDefBuff)editNext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBoolTypeBuff)editNext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBoolColBuff)editNext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamDateDefBuff)editNext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamDateTypeBuff)editNext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamDateColBuff)editNext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamDoubleDefBuff)editNext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamDoubleTypeBuff)editNext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamDoubleColBuff)editNext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamFloatDefBuff)editNext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamFloatTypeBuff)editNext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamFloatColBuff)editNext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamInt16DefBuff)editNext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamInt16TypeBuff)editNext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamId16GenBuff)editNext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamEnumDefBuff)editNext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamEnumTypeBuff)editNext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamInt16ColBuff)editNext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamInt32DefBuff)editNext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamInt32TypeBuff)editNext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamId32GenBuff)editNext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamInt32ColBuff)editNext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamInt64DefBuff)editNext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamInt64TypeBuff)editNext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamId64GenBuff)editNext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamInt64ColBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamNmTokenDefBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamNmTokenTypeBuff)editNext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamNmTokenColBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamNmTokensDefBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamNmTokensTypeBuff)editNext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamNmTokensColBuff)editNext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamNumberDefBuff)editNext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamNumberTypeBuff)editNext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamNumberColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamDbKeyHash128DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamDbKeyHash128ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamDbKeyHash128TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamDbKeyHash128GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamDbKeyHash160DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamDbKeyHash160ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamDbKeyHash160TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamDbKeyHash160GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamDbKeyHash224DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamDbKeyHash224ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamDbKeyHash224TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamDbKeyHash224GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamDbKeyHash256DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamDbKeyHash256ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamDbKeyHash256TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamDbKeyHash256GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamDbKeyHash384DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamDbKeyHash384ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamDbKeyHash384TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamDbKeyHash384GenBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamDbKeyHash512DefBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamDbKeyHash512ColBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamDbKeyHash512TypeBuff)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamDbKeyHash512GenBuff)editNext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamStringDefBuff)editNext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamStringTypeBuff)editNext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamStringColBuff)editNext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamTZDateDefBuff)editNext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamTZDateTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamTZDateColBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamTZTimeDefBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamTZTimeTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamTZTimeColBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamTZTimestampDefBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamTZTimestampTypeBuff)editNext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamTZTimestampColBuff)editNext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamTextDefBuff)editNext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamTextTypeBuff)editNext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamTextColBuff)editNext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamTimeDefBuff)editNext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamTimeTypeBuff)editNext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamTimeColBuff)editNext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamTimestampDefBuff)editNext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamTimestampTypeBuff)editNext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamTimestampColBuff)editNext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamTokenDefBuff)editNext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamTokenTypeBuff)editNext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamTokenColBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamUInt16DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamUInt16TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamUInt16ColBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamUInt32DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamUInt32TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamUInt32ColBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamUInt64DefBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamUInt64TypeBuff)editNext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamUInt64ColBuff)editNext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamUuidDefBuff)editNext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamUuidTypeBuff)editNext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamUuidGenBuff)editNext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamUuidColBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamUuid6DefBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamUuid6TypeBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamUuid6GenBuff)editNext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamUuid6ColBuff)editNext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamTableColBuff)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-edit-next-", "Not " + Integer.toString(classCode));
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckReferencingTableCols[] = schema.getTableTableCol().readDerivedByDataIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckReferencingTableCols.length > 0 ) {
			schema.getTableTableCol().deleteTableColByDataIdx( Authorization,
						existing.getRequiredId() );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckReferencingIndexCols[] = schema.getTableIndexCol().readDerivedByColIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckReferencingIndexCols.length > 0 ) {
			schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						existing.getRequiredId() );
		}
		// Validate reverse foreign keys

		if( schema.getTableBlobDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"BlobDef",
				pkey );
		}

		if( schema.getTableBoolDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"BoolDef",
				pkey );
		}

		if( schema.getTableDateDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DateDef",
				pkey );
		}

		if( schema.getTableDoubleDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DoubleDef",
				pkey );
		}

		if( schema.getTableFloatDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"FloatDef",
				pkey );
		}

		if( schema.getTableInt16Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"Int16Def",
				pkey );
		}

		if( schema.getTableInt32Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"Int32Def",
				pkey );
		}

		if( schema.getTableInt64Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"Int64Def",
				pkey );
		}

		if( schema.getTableNmTokenDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"NmTokenDef",
				pkey );
		}

		if( schema.getTableNmTokensDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"NmTokensDef",
				pkey );
		}

		if( schema.getTableNumberDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"NumberDef",
				pkey );
		}

		if( schema.getTableDbKeyHash128Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DbKeyHash128Def",
				pkey );
		}

		if( schema.getTableDbKeyHash160Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DbKeyHash160Def",
				pkey );
		}

		if( schema.getTableDbKeyHash224Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DbKeyHash224Def",
				pkey );
		}

		if( schema.getTableDbKeyHash256Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DbKeyHash256Def",
				pkey );
		}

		if( schema.getTableDbKeyHash384Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DbKeyHash384Def",
				pkey );
		}

		if( schema.getTableDbKeyHash512Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"DbKeyHash512Def",
				pkey );
		}

		if( schema.getTableStringDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"StringDef",
				pkey );
		}

		if( schema.getTableTZDateDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TZDateDef",
				pkey );
		}

		if( schema.getTableTZTimeDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TZTimeDef",
				pkey );
		}

		if( schema.getTableTZTimestampDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TZTimestampDef",
				pkey );
		}

		if( schema.getTableTextDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TextDef",
				pkey );
		}

		if( schema.getTableTimeDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TimeDef",
				pkey );
		}

		if( schema.getTableTimestampDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TimestampDef",
				pkey );
		}

		if( schema.getTableTokenDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"TokenDef",
				pkey );
		}

		if( schema.getTableUInt16Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"UInt16Def",
				pkey );
		}

		if( schema.getTableUInt32Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"UInt32Def",
				pkey );
		}

		if( schema.getTableUInt64Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"UInt64Def",
				pkey );
		}

		if( schema.getTableUuidDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"UuidDef",
				pkey );
		}

		if( schema.getTableUuid6Def().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteAtom",
				"Superclass",
				"SuperClass",
				"Uuid6Def",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffAtom > subdict;

		dictByPKey.remove( pkey );

		schema.getTableValue().deleteValue( Authorization,
			Buff );
	}
	public void deleteAtomByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteAtomByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamAtom cur;
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffValueByUNameIdxKey key = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteAtomByUNameIdx( Authorization, key );
	}

	public void deleteAtomByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamValueByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByUNameIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffValueByScopeIdxKey key = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteAtomByScopeIdx( Authorization, key );
	}

	public void deleteAtomByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamValueByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByScopeIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffValueByDefSchemaIdxKey key = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteAtomByDefSchemaIdx( Authorization, key );
	}

	public void deleteAtomByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamValueByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByDefSchemaIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByPrevIdxKey key = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteAtomByPrevIdx( Authorization, key );
	}

	public void deleteAtomByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByPrevIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByNextIdxKey key = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteAtomByNextIdx( Authorization, key );
	}

	public void deleteAtomByNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByNextIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByNextIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByContPrevIdxKey key = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalPrevId( argPrevId );
		deleteAtomByContPrevIdx( Authorization, key );
	}

	public void deleteAtomByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByContPrevIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public void deleteAtomByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByContNextIdxKey key = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalNextId( argNextId );
		deleteAtomByContNextIdx( Authorization, key );
	}

	public void deleteAtomByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContNextIdxKey argKey )
	{
		final String S_ProcName = "deleteAtomByContNextIdx";
		ICFBamAtom cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamAtom> matchSet = new LinkedList<ICFBamAtom>();
		Iterator<ICFBamAtom> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamAtom> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableAtom().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			String subClassCode = cur.getClassCode();
			if( "a80a".equals( subClassCode ) ) {
				schema.getTableAtom().deleteAtom( Authorization, cur );
			}
			else if( "a80b".equals( subClassCode ) ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( "a80c".equals( subClassCode ) ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( "a86b".equals( subClassCode ) ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( "a80d".equals( subClassCode ) ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( "a80e".equals( subClassCode ) ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( "a86c".equals( subClassCode ) ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( "a815".equals( subClassCode ) ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( "a816".equals( subClassCode ) ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( "a86d".equals( subClassCode ) ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( "a81c".equals( subClassCode ) ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( "a81d".equals( subClassCode ) ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( "a86e".equals( subClassCode ) ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( "a81f".equals( subClassCode ) ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( "a820".equals( subClassCode ) ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( "a871".equals( subClassCode ) ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( "a823".equals( subClassCode ) ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( "a824".equals( subClassCode ) ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( "a872".equals( subClassCode ) ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( "a86f".equals( subClassCode ) ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( "a870".equals( subClassCode ) ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( "a875".equals( subClassCode ) ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( "a825".equals( subClassCode ) ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( "a826".equals( subClassCode ) ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( "a873".equals( subClassCode ) ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( "a876".equals( subClassCode ) ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( "a827".equals( subClassCode ) ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( "a828".equals( subClassCode ) ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( "a874".equals( subClassCode ) ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( "a877".equals( subClassCode ) ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( "a829".equals( subClassCode ) ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( "a82a".equals( subClassCode ) ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( "a878".equals( subClassCode ) ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( "a82b".equals( subClassCode ) ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( "a82c".equals( subClassCode ) ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( "a879".equals( subClassCode ) ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( "a82d".equals( subClassCode ) ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( "a82e".equals( subClassCode ) ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( "a87a".equals( subClassCode ) ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( "a839".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( "a838".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( "a83a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( "a83b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( "a83d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( "a83c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( "a83e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( "a83f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( "a841".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( "a840".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( "a842".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( "a843".equals( subClassCode ) ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( "a845".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( "a844".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( "a846".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( "a847".equals( subClassCode ) ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( "a849".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( "a848".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( "a84a".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( "a84b".equals( subClassCode ) ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( "a84d".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( "a84c".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( "a84e".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( "a84f".equals( subClassCode ) ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( "a850".equals( subClassCode ) ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( "a851".equals( subClassCode ) ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( "a87b".equals( subClassCode ) ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( "a852".equals( subClassCode ) ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( "a853".equals( subClassCode ) ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( "a87c".equals( subClassCode ) ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( "a854".equals( subClassCode ) ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( "a855".equals( subClassCode ) ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( "a87d".equals( subClassCode ) ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( "a856".equals( subClassCode ) ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( "a857".equals( subClassCode ) ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( "a87e".equals( subClassCode ) ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( "a859".equals( subClassCode ) ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( "a85a".equals( subClassCode ) ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( "a87f".equals( subClassCode ) ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( "a85b".equals( subClassCode ) ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( "a85c".equals( subClassCode ) ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( "a880".equals( subClassCode ) ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( "a85d".equals( subClassCode ) ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( "a85e".equals( subClassCode ) ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( "a881".equals( subClassCode ) ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( "a85f".equals( subClassCode ) ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( "a860".equals( subClassCode ) ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( "a882".equals( subClassCode ) ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( "a861".equals( subClassCode ) ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( "a862".equals( subClassCode ) ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( "a883".equals( subClassCode ) ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( "a863".equals( subClassCode ) ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( "a864".equals( subClassCode ) ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( "a884".equals( subClassCode ) ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( "a865".equals( subClassCode ) ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( "a866".equals( subClassCode ) ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( "a885".equals( subClassCode ) ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( "a867".equals( subClassCode ) ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( "a869".equals( subClassCode ) ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( "a888".equals( subClassCode ) ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( "a886".equals( subClassCode ) ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( "a868".equals( subClassCode ) ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( "a86a".equals( subClassCode ) ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( "a889".equals( subClassCode ) ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( "a887".equals( subClassCode ) ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-by-suffix-class-walker-", "Not " + Integer.toString(classCode));
			}
		}
	}
}
