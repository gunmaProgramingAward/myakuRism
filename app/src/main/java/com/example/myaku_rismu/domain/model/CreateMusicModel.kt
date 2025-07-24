package com.example.myaku_rismu.domain.model

import com.example.myaku_rismu.data.constants.SubStyleConstants
import com.example.myaku_rismu.data.model.RecordType

data class CreateMusicModel(
    val recordType: RecordType?,
    val bpm: Int,
    val isInstrumental: Boolean,
    val currentDate: String,
)

sealed class HIPHOPSubGenreModel(val description: String) {
    data object OldSchool : HIPHOPSubGenreModel(SubStyleConstants.OLD_SCHOOL)
    data object Trap : HIPHOPSubGenreModel(SubStyleConstants.TRAP)
    data object Drill : HIPHOPSubGenreModel(SubStyleConstants.DRILL)

    companion object {
        private val values = listOf(OldSchool, Trap, Drill)
        fun random() = values.random()
    }
}

sealed class EDMSubGenreModel(val description: String) {
    data object House : EDMSubGenreModel(SubStyleConstants.HOUSE)
    data object Trance : EDMSubGenreModel(SubStyleConstants.TRANCE)
    data object Dubstep : EDMSubGenreModel(SubStyleConstants.DUBSTEP)

    companion object {
        private val values = listOf(House, Trance, Dubstep)
        fun random() = values.random()
    }
}

sealed class ClassicSubGenreModel(val description: String) {
    data object Baroque : ClassicSubGenreModel(SubStyleConstants.BAROQUE)
    data object Romantic : ClassicSubGenreModel(SubStyleConstants.ROMANTIC)
    data object Contemporary : ClassicSubGenreModel(SubStyleConstants.CONTEMPORARY)

    companion object {
        private val values = listOf(Baroque, Romantic, Contemporary)
        fun random() = values.random()
    }
}

sealed class POPSSubGenreModel(val description: String) {
    data object SynthPop : POPSSubGenreModel(SubStyleConstants.SYNTH_POP)
    data object IndiePop : POPSSubGenreModel(SubStyleConstants.INDIE_POP)
    data object KPop : POPSSubGenreModel(SubStyleConstants.K_POP)

    companion object {
        private val values = listOf(SynthPop, IndiePop, KPop)
        fun random() = values.random()
    }
}