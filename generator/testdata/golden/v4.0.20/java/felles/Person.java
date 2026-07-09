package no.novari.fint.model.felles;

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
import no.novari.fint.model.FintModelObject;
import no.novari.fint.model.FintIdentifikator;
import no.novari.fint.model.FintRelation;
import no.novari.fint.model.felles.kompleksedatatyper.Adresse;
import java.util.Date;
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator;
import no.novari.fint.model.felles.kompleksedatatyper.Personnavn;
import no.novari.fint.model.felles.basisklasser.Aktor;

import static no.novari.fint.model.FintMultiplicity.ONE_TO_ONE;
import static no.novari.fint.model.FintMultiplicity.ONE_TO_MANY;
import static no.novari.fint.model.FintMultiplicity.NONE_TO_ONE;
import static no.novari.fint.model.FintMultiplicity.NONE_TO_MANY;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class Person extends Aktor  implements FintModelObject {
    @Getter
    public enum Relasjonsnavn implements FintRelation {
        STATSBORGERSKAP("statsborgerskap", "no.novari.fint.model.felles.kodeverk.iso.Landkode", NONE_TO_MANY, null, null),
        KOMMUNE("kommune", "no.novari.fint.model.felles.kodeverk.Kommune", NONE_TO_ONE, null, null),
        KJONN("kjonn", "no.novari.fint.model.felles.kodeverk.iso.Kjonn", NONE_TO_ONE, null, null),
        FORELDREANSVAR("foreldreansvar", "no.novari.fint.model.felles.Person", NONE_TO_MANY, true, "foreldre"),
        MALFORM("malform", "no.novari.fint.model.felles.kodeverk.iso.Sprak", NONE_TO_ONE, null, null),
        PERSONALRESSURS("personalressurs", "no.novari.fint.model.administrasjon.personal.Personalressurs", NONE_TO_ONE, true, "person"),
        MORSMAL("morsmal", "no.novari.fint.model.felles.kodeverk.iso.Sprak", NONE_TO_ONE, null, null),
        PARORENDE("parorende", "no.novari.fint.model.felles.Kontaktperson", NONE_TO_MANY, false, "kontaktperson"),
        FORELDRE("foreldre", "no.novari.fint.model.felles.Person", NONE_TO_MANY, true, "foreldreansvar"),
        LARLING("larling", "no.novari.fint.model.utdanning.larling.Larling", NONE_TO_MANY, false, "person"),
        ELEV("elev", "no.novari.fint.model.utdanning.elev.Elev", NONE_TO_ONE, false, "person"),
        OTUNGDOM("otungdom", "no.novari.fint.model.utdanning.ot.OtUngdom", NONE_TO_ONE, false, "person");
    
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
    public Map<String, FintIdentifikator> getIdentifikators() {
        Map<String, FintIdentifikator> identifikators = new HashMap<>();
        identifikators.put("fodselsnummer", this.fodselsnummer);

        return Collections.unmodifiableMap(identifikators);
    }
    @JsonIgnore
    private List<FintRelation> createRelations() {
        List<FintRelation> relations = new ArrayList<>();

        relations.addAll(Arrays.asList(Relasjonsnavn.values()));

        return Collections.unmodifiableList(relations);
    }

    @JsonIgnore
    private final List<FintRelation> relations = createRelations();
    private String bilde;
    private @Valid Adresse bostedsadresse;
    private Date fodselsdato;
    @NotNull
    private @Valid Identifikator fodselsnummer;
    @NotNull
    private @Valid Personnavn navn;
}
