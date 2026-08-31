package no.novari.fint.core.model

import kotlin.reflect.KClass

/**
 * Registry over every type in the FINT model.
 *
 * Use [byPath] to find a resource from the three parts of a REST path,
 * follow a relation with [FintRelation.targetMetadata], or list everything
 * the model serves with [paths] and [refs].
 */
object FintModel {

    /** Metadata for every concrete type in the model. */
    val types: List<FintTypeMetadata> = listOf(
        no.novari.fint.core.model.arkiv.kodeverk.DokumentStatus.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.DokumentType.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Format.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.JournalpostType.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.JournalStatus.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Klassifikasjonstype.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.KorrespondansepartType.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Merknadstype.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.PartRolle.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Rolle.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Saksmappetype.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Saksstatus.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Skjermingshjemmel.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Tilgangsgruppe.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Tilgangsrestriksjon.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.TilknyttetRegistreringSom.Metadata,
        no.novari.fint.core.model.arkiv.kodeverk.Variantformat.Metadata,
        no.novari.fint.core.model.arkiv.personal.Personalmappe.Metadata,
        no.novari.fint.core.model.arkiv.samferdsel.SoknadDrosjeloyve.Metadata,
        no.novari.fint.core.model.arkiv.kulturminnevern.DispensasjonAutomatiskFredaKulturminne.Metadata,
        no.novari.fint.core.model.arkiv.kulturminnevern.TilskuddFartoy.Metadata,
        no.novari.fint.core.model.arkiv.kulturminnevern.TilskuddFredaBygningPrivatEie.Metadata,
        no.novari.fint.core.model.arkiv.noark.AdministrativEnhet.Metadata,
        no.novari.fint.core.model.arkiv.noark.Arkivdel.Metadata,
        no.novari.fint.core.model.arkiv.noark.Arkivressurs.Metadata,
        no.novari.fint.core.model.arkiv.noark.Autorisasjon.Metadata,
        no.novari.fint.core.model.arkiv.noark.Avskrivning.Metadata,
        no.novari.fint.core.model.arkiv.noark.Dokumentbeskrivelse.Metadata,
        no.novari.fint.core.model.arkiv.noark.Dokumentfil.Metadata,
        no.novari.fint.core.model.arkiv.noark.Dokumentobjekt.Metadata,
        no.novari.fint.core.model.arkiv.noark.Journalpost.Metadata,
        no.novari.fint.core.model.arkiv.noark.Klasse.Metadata,
        no.novari.fint.core.model.arkiv.noark.Klassifikasjonssystem.Metadata,
        no.novari.fint.core.model.arkiv.noark.Korrespondansepart.Metadata,
        no.novari.fint.core.model.arkiv.noark.Merknad.Metadata,
        no.novari.fint.core.model.arkiv.noark.Part.Metadata,
        no.novari.fint.core.model.arkiv.noark.Sak.Metadata,
        no.novari.fint.core.model.arkiv.noark.Skjerming.Metadata,
        no.novari.fint.core.model.arkiv.noark.Tilgang.Metadata,
        no.novari.fint.core.model.okonomi.faktura.Faktura.Metadata,
        no.novari.fint.core.model.okonomi.faktura.Fakturagrunnlag.Metadata,
        no.novari.fint.core.model.okonomi.faktura.Fakturalinje.Metadata,
        no.novari.fint.core.model.okonomi.faktura.Fakturamottaker.Metadata,
        no.novari.fint.core.model.okonomi.faktura.Fakturautsteder.Metadata,
        no.novari.fint.core.model.okonomi.kodeverk.Merverdiavgift.Metadata,
        no.novari.fint.core.model.okonomi.kodeverk.Vare.Metadata,
        no.novari.fint.core.model.okonomi.regnskap.Bilag.Metadata,
        no.novari.fint.core.model.okonomi.regnskap.Leverandor.Metadata,
        no.novari.fint.core.model.okonomi.regnskap.Leverandorgruppe.Metadata,
        no.novari.fint.core.model.okonomi.regnskap.Postering.Metadata,
        no.novari.fint.core.model.okonomi.regnskap.Transaksjon.Metadata,
        no.novari.fint.core.model.personvern.kodeverk.Behandlingsgrunnlag.Metadata,
        no.novari.fint.core.model.personvern.kodeverk.Personopplysning.Metadata,
        no.novari.fint.core.model.personvern.samtykke.Behandling.Metadata,
        no.novari.fint.core.model.personvern.samtykke.Samtykke.Metadata,
        no.novari.fint.core.model.personvern.samtykke.Tjeneste.Metadata,
        no.novari.fint.core.model.felles.Kontaktperson.Metadata,
        no.novari.fint.core.model.felles.Person.Metadata,
        no.novari.fint.core.model.felles.Virksomhet.Metadata,
        no.novari.fint.core.model.felles.kodeverk.Fylke.Metadata,
        no.novari.fint.core.model.felles.kodeverk.Kommune.Metadata,
        no.novari.fint.core.model.felles.kodeverk.Valuta.Metadata,
        no.novari.fint.core.model.felles.kodeverk.iso.Kjonn.Metadata,
        no.novari.fint.core.model.felles.kodeverk.iso.Landkode.Metadata,
        no.novari.fint.core.model.felles.kodeverk.iso.Sprak.Metadata,
        no.novari.fint.core.model.felles.kompleksedatatyper.Adresse.Metadata,
        no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator.Metadata,
        no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon.Metadata,
        no.novari.fint.core.model.felles.kompleksedatatyper.Matrikkelnummer.Metadata,
        no.novari.fint.core.model.felles.kompleksedatatyper.Periode.Metadata,
        no.novari.fint.core.model.felles.kompleksedatatyper.Personnavn.Metadata,
        no.novari.fint.core.model.administrasjon.fullmakt.Fullmakt.Metadata,
        no.novari.fint.core.model.administrasjon.fullmakt.Rolle.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Aktivitet.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Anlegg.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Ansvar.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Arbeidsforholdstype.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Art.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Diverse.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Formal.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Fravarsgrunn.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Fravarstype.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Funksjon.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Kontrakt.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Lonnsart.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Lopenummer.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Objekt.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Organisasjonstype.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Personalressurskategori.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Prosjekt.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Prosjektart.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Ramme.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Stillingskode.Metadata,
        no.novari.fint.core.model.administrasjon.kodeverk.Uketimetall.Metadata,
        no.novari.fint.core.model.administrasjon.organisasjon.Arbeidslokasjon.Metadata,
        no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement.Metadata,
        no.novari.fint.core.model.administrasjon.personal.Fastlonn.Metadata,
        no.novari.fint.core.model.administrasjon.personal.Fasttillegg.Metadata,
        no.novari.fint.core.model.administrasjon.personal.Fravar.Metadata,
        no.novari.fint.core.model.administrasjon.personal.Variabellonn.Metadata,
        no.novari.fint.core.model.administrasjon.personal.Personalressurs.Metadata,
        no.novari.fint.core.model.administrasjon.personal.Arbeidsforhold.Metadata,
        no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng.Metadata,
        no.novari.fint.core.model.utdanning.larling.AvlagtProve.Metadata,
        no.novari.fint.core.model.utdanning.larling.Larling.Metadata,
        no.novari.fint.core.model.utdanning.ot.OtUngdom.Metadata,
        no.novari.fint.core.model.utdanning.elev.Elev.Metadata,
        no.novari.fint.core.model.utdanning.elev.Elevforhold.Metadata,
        no.novari.fint.core.model.utdanning.elev.Elevtilrettelegging.Metadata,
        no.novari.fint.core.model.utdanning.elev.Klasse.Metadata,
        no.novari.fint.core.model.utdanning.elev.Klassemedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.elev.Kontaktlarergruppe.Metadata,
        no.novari.fint.core.model.utdanning.elev.Kontaktlarergruppemedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.elev.Persongruppe.Metadata,
        no.novari.fint.core.model.utdanning.elev.Persongruppemedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.elev.Skoleressurs.Metadata,
        no.novari.fint.core.model.utdanning.elev.Undervisningsforhold.Metadata,
        no.novari.fint.core.model.utdanning.elev.Varsel.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Eksamen.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Fag.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Faggruppe.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Faggruppemedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Rom.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Time.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Undervisningsgruppe.Metadata,
        no.novari.fint.core.model.utdanning.timeplan.Undervisningsgruppemedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.utdanningsprogram.Arstrinn.Metadata,
        no.novari.fint.core.model.utdanning.utdanningsprogram.Programomrade.Metadata,
        no.novari.fint.core.model.utdanning.utdanningsprogram.Programomrademedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.utdanningsprogram.Skole.Metadata,
        no.novari.fint.core.model.utdanning.utdanningsprogram.Utdanningsprogram.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Aktivitetsfravar.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Anmerkninger.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppe.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppemedlemskap.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Eksamensvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Elevfravar.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Elevvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Fravarsoversikt.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Fravarsprosent.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Fravarsregistrering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Halvarsfagvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Halvarsordensvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Karakterhistorie.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Karakterverdi.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Sensor.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Sluttfagvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Sluttordensvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Underveisfagvurdering.Metadata,
        no.novari.fint.core.model.utdanning.vurdering.Underveisordensvurdering.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Avbruddsarsak.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Betalingsstatus.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Bevistype.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Brevtype.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Eksamensform.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Elevkategori.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Fagmerknad.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Fagstatus.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Fravarstype.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Fullfortkode.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Grepreferanse.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Karakterskala.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Karakterstatus.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.OtEnhet.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.OtStatus.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Provestatus.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Skolear.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Skoleeiertype.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Termin.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Tilrettelegging.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Varseltype.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Vigoreferanse.Metadata,
        no.novari.fint.core.model.utdanning.kodeverk.Vitnemalsmerknad.Metadata,
        no.novari.fint.core.model.ressurs.datautstyr.DigitalEnhet.Metadata,
        no.novari.fint.core.model.ressurs.datautstyr.Enhetsgruppe.Metadata,
        no.novari.fint.core.model.ressurs.datautstyr.Enhetsgruppemedlemskap.Metadata,
        no.novari.fint.core.model.ressurs.eiendel.Applikasjon.Metadata,
        no.novari.fint.core.model.ressurs.eiendel.Applikasjonsressurs.Metadata,
        no.novari.fint.core.model.ressurs.eiendel.Applikasjonsressurstilgjengelighet.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Applikasjonskategori.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Brukertype.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Enhetstype.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Handhevingstype.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Lisensmodell.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Plattform.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Produsent.Metadata,
        no.novari.fint.core.model.ressurs.kodeverk.Status.Metadata,
        no.novari.fint.core.model.ressurs.tilgang.Identitet.Metadata,
        no.novari.fint.core.model.ressurs.tilgang.Rettighet.Metadata,
    )

    /** Metadata for every resource: the types that can carry links. */
    val resources: List<FintResourceMetadata> = types.filterIsInstance<FintResourceMetadata>()

    internal val typeIndex: Map<KClass<*>, FintTypeMetadata> = types.associateBy { it.type }

    private val refIndex: Map<String, FintResourceMetadata> =
        resources.mapNotNull { meta ->
            meta.path?.lowercase()?.split('/')?.let { "${it.first()}/${it[1]}/${it.last()}" to meta }
        }.toMap()

    private val commonIndex: Map<String, FintResourceMetadata> =
        resources.filter { it.isCommon }.associateBy { it.name.lowercase() }

    /**
     * Finds the resource served at /[domainName]/[packageName]/[resourceName].
     * Returns null when no such resource exists. Case does not matter.
     *
     * A common resource answers under the domain and package it is served
     * through, so byPath("utdanning", "elev", "person") and
     * byPath("administrasjon", "personal", "person") both find felles:Person.
     * A felles/kodeverk/iso resource answers at its identity, so
     * byPath("felles", "kodeverk", "landkode") finds Landkode even though its
     * path keeps the extra segment.
     */
    fun byPath(domainName: String, packageName: String, resourceName: String): FintResourceMetadata? =
        refIndex["$domainName/$packageName/$resourceName".lowercase()]
            ?: commonIndex[resourceName.lowercase()]

    /** Metadata for [type], or null when it is not a type from the model. */
    fun byType(type: KClass<*>): FintTypeMetadata? = typeIndex[type]

    /**
     * Every REST path the model serves.
     *
     * Holds every resource's own [FintResourceMetadata.path], and adds each
     * common resource under every domain and package it can be reached from:
     * Elev links to Person, so "utdanning/elev/person" is included. The walk
     * also follows relations between common resources until nothing new
     * appears: Person links to Kontaktperson, so "utdanning/elev/kontaktperson"
     * is included even though no elev resource links to Kontaktperson directly.
     *
     * These are the paths as URLs show them, so the felles/kodeverk/iso
     * entries keep their extra segment. [refs] holds the same set as
     * identities.
     */
    val paths: Set<String> by lazy {
        val result = resources
            .flatMap { meta -> listOfNotNull(meta.path) + meta.relations.mapNotNull { meta.relationPath(it.name) } }
            .toMutableSet()
        var grew = true
        while (grew) {
            grew = false
            for (path in result.toList()) {
                val (domainName, packageName, resourceName) = path.split('/').takeIf { it.size == 3 } ?: continue
                val meta = byPath(domainName, packageName, resourceName)?.takeIf { it.isCommon } ?: continue
                for (relation in meta.relations) {
                    meta.relationPath(relation.name, path)?.let { if (result.add(it)) grew = true }
                }
            }
        }
        result
    }

    /**
     * Every place the model serves, as identities: one [FintResourceRef] for
     * each entry in [paths].
     *
     * Identity and path name the same three parts for every resource except
     * the felles/kodeverk/iso three, whose extra path segment the identity
     * drops: "felles/kodeverk/iso/landkode" appears here as
     * ("felles", "kodeverk", "landkode"). Every entry resolves through
     * [byPath], and [refsIn] narrows to one domain and package.
     */
    val refs: Set<FintResourceRef> by lazy { refByPath.values.toSet() }

    /**
     * The identities served under /[domainName]/[packageName], empty when the
     * model serves nothing there. Case does not matter.
     *
     * Common resources show up under every domain and package that can reach
     * them, so refsIn("utdanning", "elev") includes person and kontaktperson,
     * and the felles/kodeverk/iso resources show up collapsed, so
     * refsIn("felles", "kodeverk") includes landkode.
     */
    fun refsIn(domainName: String, packageName: String): Set<FintResourceRef> =
        refs.filter {
            it.domainName.equals(domainName, ignoreCase = true) &&
                it.packageName.equals(packageName, ignoreCase = true)
        }.toSet()

    /**
     * The identity of the resource served at [path], or null when the model
     * serves nothing at that path. Case does not matter, and a leading or
     * trailing "/" is ignored.
     *
     * [path] is matched against [paths], so a felles/kodeverk/iso path answers
     * with its extra segment dropped, refOf("felles/kodeverk/iso/landkode") is
     * ("felles", "kodeverk", "landkode"), while the already collapsed
     * "felles/kodeverk/landkode" is not a served path and answers null.
     */
    fun refOf(path: String): FintResourceRef? = refByPath[path.trim('/').lowercase()]

    private val refByPath: Map<String, FintResourceRef> by lazy {
        paths.associateWith { path ->
            path.split('/').let { FintResourceRef(it.first(), it[1], it.last()) }
        }
    }
}

/** Metadata for the type this relation points to, or null for targets outside the model. */
val FintRelation.targetMetadata: FintTypeMetadata?
    get() = FintModel.typeIndex[target]
