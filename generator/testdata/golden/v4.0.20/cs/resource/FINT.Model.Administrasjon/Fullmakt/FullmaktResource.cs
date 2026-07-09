using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Administrasjon.Fullmakt
{

    public class FullmaktResource 
    {

    
        public Periode Gyldighetsperiode { get; set; }
        public Identifikator SystemId { get; set; }
        
        public FullmaktResource()
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
     
            

        public void AddRamme(Link link)
        {
            AddLink("ramme", link);
        }

        public void AddFunksjon(Link link)
        {
            AddLink("funksjon", link);
        }

        public void AddObjekt(Link link)
        {
            AddLink("objekt", link);
        }

        public void AddOrganisasjonselement(Link link)
        {
            AddLink("organisasjonselement", link);
        }

        public void AddArt(Link link)
        {
            AddLink("art", link);
        }

        public void AddAnlegg(Link link)
        {
            AddLink("anlegg", link);
        }

        public void AddDiverse(Link link)
        {
            AddLink("diverse", link);
        }

        public void AddAktivitet(Link link)
        {
            AddLink("aktivitet", link);
        }

        public void AddAnsvar(Link link)
        {
            AddLink("ansvar", link);
        }

        public void AddStedfortreder(Link link)
        {
            AddLink("stedfortreder", link);
        }

        public void AddKontrakt(Link link)
        {
            AddLink("kontrakt", link);
        }

        public void AddFullmektig(Link link)
        {
            AddLink("fullmektig", link);
        }

        public void AddProsjekt(Link link)
        {
            AddLink("prosjekt", link);
        }

        public void AddFormal(Link link)
        {
            AddLink("formal", link);
        }

        public void AddRolle(Link link)
        {
            AddLink("rolle", link);
        }

        public void AddLopenummer(Link link)
        {
            AddLink("lopenummer", link);
        }
    }
}
