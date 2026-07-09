package no.novari.fint.model.arkiv.noark;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.novari.fint.model.FintMultiplicity;
import no.novari.fint.model.FintComplexDatatypeObject;
import no.novari.fint.model.FintIdentifikator;
import no.novari.fint.model.FintRelation;
import no.novari.fint.model.arkiv.noark.Dokumentobjekt;
import java.util.Date;
import no.novari.fint.model.arkiv.noark.Part;
import no.novari.fint.model.arkiv.noark.Skjerming;

import static no.novari.fint.model.FintMultiplicity.ONE_TO_ONE;
import static no.novari.fint.model.FintMultiplicity.ONE_TO_MANY;
import static no.novari.fint.model.FintMultiplicity.NONE_TO_ONE;
import static no.novari.fint.model.FintMultiplicity.NONE_TO_MANY;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Dokumentbeskrivelse  implements FintComplexDatatypeObject {
    @Getter
    public enum Relasjonsnavn implements FintRelation {
        DOKUMENTSTATUS("dokumentstatus", "no.novari.fint.model.arkiv.kodeverk.DokumentStatus", ONE_TO_ONE, null, null),
        DOKUMENTTYPE("dokumentType", "no.novari.fint.model.arkiv.kodeverk.DokumentType", ONE_TO_ONE, null, null),
        TILKNYTTETREGISTRERINGSOM("tilknyttetRegistreringSom", "no.novari.fint.model.arkiv.kodeverk.TilknyttetRegistreringSom", ONE_TO_MANY, null, null),
        TILKNYTTETAV("tilknyttetAv", "no.novari.fint.model.arkiv.noark.Arkivressurs", ONE_TO_ONE, null, null),
        OPPRETTETAV("opprettetAv", "no.novari.fint.model.arkiv.noark.Arkivressurs", ONE_TO_ONE, null, null);
    
        private final String name;
        private final String packageName;
        private final FintMultiplicity multiplicity;
        private final String inverseName;
        private final Boolean isSource;

        private Relasjonsnavn(String name, String packageName, FintMultiplicity multiplicity, Boolean isSource, String inverseName) {
            this.name = name;
            this.packageName = packageName;
            this.multiplicity = multiplicity;
            this.inverseName = inverseName;
            this.isSource = isSource;
        }
    }

    @JsonIgnore
    private List<FintRelation> createRelations() {
        List<FintRelation> relations = new ArrayList<>();

        relations.addAll(Arrays.asList(Relasjonsnavn.values()));

        return Collections.unmodifiableList(relations);
    }

    @JsonIgnore
    private final List<FintRelation> relations = createRelations();
    private String beskrivelse;
    private Long dokumentnummer;
    private List<@Valid Dokumentobjekt> dokumentobjekt;
    private List<String> forfatter;
    private Date opprettetDato;
    private List<@Valid Part> part;
    private List<String> referanseArkivdel;
    private @Valid Skjerming skjerming;
    private Date tilknyttetDato;
    @NotBlank
    private String tittel;
}
