using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Eiendel
{

    public class ApplikasjonsressursResource 
    {

    
        public string Beskrivelse { get; set; }
        public long? Enhetskostnad { get; set; }
        public Periode Gyldighetsperiode { get; set; }
        public bool? KreverGodkjenning { get; set; }
        public long? Lisensantall { get; set; }
        public string Navn { get; set; }
        public Identifikator SystemId { get; set; }
        
        public ApplikasjonsressursResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddBrukertype(Link link)
        {
            AddLink("brukertype", link);
        }

        public void AddHandhevingstype(Link link)
        {
            AddLink("handhevingstype", link);
        }

        public void AddLisensmodell(Link link)
        {
            AddLink("lisensmodell", link);
        }

        public void AddRessurstilgjengelighet(Link link)
        {
            AddLink("ressurstilgjengelighet", link);
        }

        public void AddEier(Link link)
        {
            AddLink("eier", link);
        }

        public void AddApplikasjon(Link link)
        {
            AddLink("applikasjon", link);
        }
    }
}
